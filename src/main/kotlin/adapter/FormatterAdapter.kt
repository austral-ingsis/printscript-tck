package adapter

import interpreter.PrintScriptFormatter
import java.io.InputStream
import java.io.Writer
import org.printscript.common.Result
import org.printscript.formatter.config.FormatterConfig
import org.printscript.lexer.source.StreamSourceReader
import org.printscript.runner.FormatRunner
import org.printscript.runner.config.loadFormatterConfig

class FormatterAdapter : PrintScriptFormatter {
    // format() devuelve void y no recibe ErrorHandler: no hay donde reportar. Ante un
    // problema no escribimos nada, que es lo unico honesto --escribir a medias dejaria
    // al que llama con un archivo truncado sin saberlo--.
    override fun format(
        src: InputStream,
        version: String,
        config: InputStream,
        writer: Writer,
    ) {
        val parsed = Version.of(version) ?: return
        val loaded = loadFormatterConfig(config.reader().readText())

        if (loaded is Result.Success) {
            write(parsed, src, loaded.value, writer)
        }
    }

    // Se escribe trozo por trozo, sin juntar la salida: la Sequence es perezosa y el
    // Writer va recibiendo. Con 7 MB de heap, un archivo entero en un String no entra.
    private fun write(
        version: Version,
        src: InputStream,
        config: FormatterConfig,
        writer: Writer,
    ) {
        for (formatted in FormatRunner(config, version).format({ StreamSourceReader.of(src) })) {
            when (formatted) {
                is Result.Success -> writer.write(formatted.value.text)
                is Result.Failure -> return
            }
        }
    }
}
