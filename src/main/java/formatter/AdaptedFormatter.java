package formatter;

import interpreter.PrintScriptFormatter;
import runner.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdaptedFormatter implements PrintScriptFormatter {
    Operations runner = new Operations();
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        try {
            String input = decode(src);
            try {
                String output = runner.format(input);
                for ( char letter : output.toCharArray()){
                    writer.append(letter);
                }
            }
            catch (Exception e) {
                return;
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
