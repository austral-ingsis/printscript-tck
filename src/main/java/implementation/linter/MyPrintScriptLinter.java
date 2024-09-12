package implementation.linter;

import implementation.JsonCreator;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlinx.serialization.json.JsonObject;
import org.Runner;
import org.RunnerResult;

import java.io.*;

import static implementation.InputStreamToStringReader.convert;

public class MyPrintScriptLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Reader reader = convert(src);
        Runner runner = new Runner(version, reader);

        JsonCreator jsonCreator = new JsonCreator();
        JsonObject json = jsonCreator.getJsonFromInputStream(config);
        JsonObject configJson = new CheckAdapter().adapt(json);

        RunnerResult.Analyze lintResult = runner.analyze(configJson);
        lintResult.getWarningsList().forEach(handler::reportError);
    }
}
