package implementation;

import interpreter.PrintScriptFormatter;
import org.Runner;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public class MyPrintScriptFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        // Your implementation here
        Runner runner = new Runner(version);
        String result = runner.format(src.toString());

        for (String message : result) {
            writer.append(message);
        }
    }
}
