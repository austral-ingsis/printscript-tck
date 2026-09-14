package implementation;

import interpreter.PrintScriptFormatter;
import kotlin.Unit;
import printscript.runner.PrintScriptRunner;

import java.io.InputStream;
import java.io.Writer;

public class PrintScriptFormatterAdapter implements PrintScriptFormatter {
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        PrintScriptRunner.INSTANCE.format(
            src,
            version,
            config,
            writer,
            err -> Unit.INSTANCE
        );
    }
}
