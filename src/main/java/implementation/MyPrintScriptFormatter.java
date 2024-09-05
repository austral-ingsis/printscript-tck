package implementation;

import interpreter.PrintScriptFormatter;
import kotlinx.serialization.json.JsonObject;
import org.Runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;


public class MyPrintScriptFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Runner runner = new Runner(version);
        JsonObject configJson = new Utils().getJsonFromStream(config);
        String result = runner.format(src.toString(), configJson).toString();
        try {
            writer.write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
