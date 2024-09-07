package interpreter;

import runner.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class AdaptedInterpreter implements PrintScriptInterpreter{

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            String input = decode(src);
            Operations runner = new Operations(input, version);
            try {
                List<String> output = runner.execute();
                for (String result : output) {
                    emitter.print(result);
                }
            }
            catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        }
        catch (IOException e) {return;}
    }

    private String decode(InputStream stream) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }
}
