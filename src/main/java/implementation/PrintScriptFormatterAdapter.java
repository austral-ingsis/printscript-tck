package implementation;

import com.printscript.formatter.Config;
import com.printscript.formatter.Formatter;

import interpreter.PrintScriptFormatter;

import java.io.InputStream;
import java.io.Writer;

public class PrintScriptFormatterAdapter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        if (!Pipeline.supports(version)) {
            throw new IllegalArgumentException("Unsupported version: " + version);
        }

        final var settings = Config.Companion.read(config);

        new Formatter(settings).format(Pipeline.lexer(src).tokens(), writer);
    }
}
