package implementation

import cli.FileInputReader
import cli.JsonConfigReader
import common.io.reporter.DiagnosticReporter
import common.model.diagnostic.Diagnostic
import interpreter.ErrorHandler
import interpreter.PrintScriptLinter as TckPrintScriptLinter
import lexer.PrintScriptLexer
import linter.LinterRunner
import linter.PrintScriptLinter as CorePrintScriptLinter
import parser.PrintScriptParser
import validator.PrintScriptValidator
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

class TckLinterAdapter : TckPrintScriptLinter {
    override fun lint(
        src: InputStream,
        version: String,
        config: InputStream,
        handler: ErrorHandler,
    ) {
        val sourcePath = Files.createTempFile("printscript-tck-source-", ".ps")

        try {
            val configPath = Files.createTempFile("printscript-tck-config-", ".json")

            try {
                Files.copy(src, sourcePath, REPLACE_EXISTING)
                Files.copy(config, configPath, REPLACE_EXISTING)

                val reporter = object : DiagnosticReporter {
                    override fun report(diagnostic: Diagnostic) {
                        handler.reportError(diagnostic.message)
                    }
                }

                LinterRunner(
                    lexer = PrintScriptLexer(),
                    parser = PrintScriptParser(),
                    validator = PrintScriptValidator(),
                    linter = CorePrintScriptLinter(),
                ).run(
                    version = version,
                    source = FileInputReader(sourcePath),
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