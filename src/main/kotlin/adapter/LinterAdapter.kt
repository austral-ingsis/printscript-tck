package adapter

import interpreter.ErrorHandler
import interpreter.PrintScriptLinter
import java.io.InputStream
import org.printscript.analyzer.Diagnostic
import org.printscript.common.Result
import org.printscript.lexer.source.StreamSourceReader
import org.printscript.runner.AnalyzeRunner
import org.printscript.runner.config.loadAnalyzerConfig

class LinterAdapter : PrintScriptLinter {
    override fun lint(
        src: InputStream,
        version: String,
        config: InputStream,
        handler: ErrorHandler,
    ) {
        when (val parsed = Version.of(version)) {
            null -> handler.reportError("Version desconocida: $version")
            else -> run(parsed, src, config, handler)
        }
    }

    private fun run(
        version: Version,
        src: InputStream,
        config: InputStream,
        handler: ErrorHandler,
    ) {
        when (val loaded = loadAnalyzerConfig(config.reader().readText())) {
            is Result.Failure -> handler.reportError(loaded.error.message)
            is Result.Success ->
                AnalyzeRunner(loaded.value, version).analyze({ StreamSourceReader.of(src) }) { diagnostic ->
                    handler.reportError(line(diagnostic))
                }
        }
    }

    // La posicion va en el mensaje porque la consigna la pide y el ErrorHandler solo
    // acepta un String: es la unica forma de no perderla al cruzar la frontera.
    private fun line(diagnostic: Diagnostic): String =
        "${diagnostic.range.start}  ${diagnostic.severity.name.lowercase()}  " +
            "${diagnostic.rule}  ${diagnostic.message}"
}
