package interpreter;

import runner.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class AdaptedInterpreter implements PrintScriptInterpreter{
    Operations runner = new Operations();
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            String input = decode(src);
            try {
                List<String> output = runner.execute(input);
                for (String result : output) {
                    emitter.print(result);
                }
            }
            catch (Error e) {
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
