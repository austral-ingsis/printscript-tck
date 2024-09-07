package implementation;

import interpreter.PrintScriptFormatter;
import org.Runner;

import java.io.*;
import java.util.stream.Collectors;


public class MyPrintScriptFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) throws IOException {
        Runner runner = new Runner(version);
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String code = reader.lines().collect(Collectors.joining("\n"));

        String result = runner.format(code, config.toString(), version).getFormattedCode();
        writer.write(result);
    }

}