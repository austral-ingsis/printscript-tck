package linter;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import runner.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import error.Error;

public class AdaptedLinter implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        try {
            String input = decode(src);
            Operations runner = new Operations(input, version);
            try {
                List<Error> output = runner.analyze();
                for ( Error error : output){
                    handler.reportError(error.toString());
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

