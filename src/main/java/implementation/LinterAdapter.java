package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import org.example.Runner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        StringBuilder configuration = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(config))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configuration.append(line.replace("\r", " ")).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> errors = new ArrayList<>();
        try {
            errors = Runner.lint(src, version, configuration.toString());
        } catch (Exception e) {
            handler.reportError(e.toString());
        }
        for (String error : errors) {
            handler.reportError(error);
        }
    }
}
