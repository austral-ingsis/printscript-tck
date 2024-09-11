package implementation.formatter;

import implementation.JsonCreator;
import interpreter.PrintScriptFormatter;
import kotlinx.serialization.json.JsonObject;
import org.Runner;
import java.io.*;

import static implementation.InputStreamToStringReader.convert;


public class MyPrintScriptFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) throws IOException {
        StringReader reader = convert(src);
        Runner runner = new Runner(version, reader);

        JsonCreator jsonCreator = new JsonCreator();
        JsonObject json = jsonCreator.getJsonFromInputStream(config);

        String result = runner.format(json.toString(), version).getFormattedCode();
        writer.write(result);
    }
}