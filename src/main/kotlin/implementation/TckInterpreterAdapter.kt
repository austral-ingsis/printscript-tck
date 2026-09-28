package implementation

import cli.ExecutionRunner
import cli.FileInputReader
import cli.SystemEnvReader
import common.io.reader.input.InputReader
import common.io.reporter.DiagnosticReporter
import common.io.writer.OutputWriter
import common.model.diagnostic.Diagnostic
import interpreter.DefaultPrintScriptInterpreter
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter as TckPrintScriptInterpreter
import lexer.PrintScriptLexer
import parser.PrintScriptParser
import validator.PrintScriptValidator
import validator.ValidatorRunner
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

class TckInterpreterAdapter : TckPrintScriptInterpreter {
    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider,
    ) {
        val sourcePath = Files.createTempFile("printscript-tck-source-", ".ps")

        try {
            Files.copy(src, sourcePath, REPLACE_EXISTING)

            val reporter = TckDiagnosticReporter(handler)

            ValidatorRunner(
                lexer = PrintScriptLexer(),
                parser = PrintScriptParser(),
                validator = PrintScriptValidator(),
            ).run(
                version = version,
                source = FileInputReader(sourcePath),
                reporter = reporter,
            )

            if (reporter.hasDiagnostics) {
                return
            }

            var lastOutput = ""

            val output = object : OutputWriter {
                override fun write(input: Sequence<String>) {
                    input.forEach { message ->
                        lastOutput = message
                        emitter.print(message)
                    }
                }
            }

            val input = object : InputReader {
                override fun read(): Sequence<Char> {
                    val value = provider.input(lastOutput)
                    return value?.asSequence() ?: emptySequence()
                }
            }

            ExecutionRunner(
                lexer = PrintScriptLexer(),
                parser = PrintScriptParser(),
                interpreter = DefaultPrintScriptInterpreter(),
            ).run(
                version = version,
                source = FileInputReader(sourcePath),
                input = input,
                output = output,
                env = SystemEnvReader(),
                reporter = reporter,
            )
        } catch (error: OutOfMemoryError) {
            handler.reportError(error.message ?: "Java heap space")
        } finally {
            Files.deleteIfExists(sourcePath)
        }
    }

    private class TckDiagnosticReporter(
        private val handler: ErrorHandler,
    ) : DiagnosticReporter {
        var hasDiagnostics: Boolean = false
            private set

        override fun report(diagnostic: Diagnostic) {
            hasDiagnostics = true
            handler.reportError(diagnostic.message)
        }
    }
}