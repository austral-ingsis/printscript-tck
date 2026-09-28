package implementation

import cli.FileInputReader
import cli.JsonConfigReader
import common.io.reporter.DiagnosticReporter
import common.io.writer.OutputWriter
import common.model.diagnostic.Diagnostic
import formatter.FormatterRunner
import formatter.PrintScriptFormatter as CorePrintScriptFormatter
import interpreter.PrintScriptFormatter as TckPrintScriptFormatter
import lexer.PrintScriptLexer
import parser.PrintScriptParser
import java.io.InputStream
import java.io.Writer
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

class TckFormatterAdapter : TckPrintScriptFormatter {
    override fun format(
        src: InputStream,
        version: String,
        config: InputStream,
        writer: Writer,
    ) {
        val sourcePath = Files.createTempFile("printscript-tck-source-", ".ps")

        try {
            val configPath = Files.createTempFile("printscript-tck-config-", ".json")

            try {
                Files.copy(src, sourcePath, REPLACE_EXISTING)
                Files.copy(config, configPath, REPLACE_EXISTING)

                val output = object : OutputWriter {
                    override fun write(input: Sequence<String>) {
                        input.forEach(writer::write)
                    }
                }

                val reporter = object : DiagnosticReporter {
                    override fun report(diagnostic: Diagnostic) {
                        throw IllegalArgumentException(diagnostic.message)
                    }
                }

                FormatterRunner(
                    lexer = PrintScriptLexer(),
                    parser = PrintScriptParser(),
                    formatter = CorePrintScriptFormatter(),
                ).run(
                    version = version,
                    source = FileInputReader(sourcePath),
                    target = output,
                    config = JsonConfigReader(configPath),
                    reporter = reporter,
                )
            } finally {
                Files.deleteIfExists(configPath)
            }
        } finally {
            Files.deleteIfExists(sourcePath)
        }
    }
}