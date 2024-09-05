package implementation;

import interpreter.PrintScriptFormatter;
import org.Runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;


public class MyPrintScriptFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) throws IOException {
        Runner runner = new Runner(version);
        String result = runner.format(src.toString(), config.toString(), version).getFormattedCode();
        writer.write(result);
    }

}