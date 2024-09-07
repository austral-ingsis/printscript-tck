package implementation.linter;

import implementation.JsonCreator;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlinx.serialization.json.JsonObject;
import org.Runner;
import org.RunnerResult;

import java.io.*;
import java.util.stream.Collectors;

public class MyPrintScriptLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Runner runner = new Runner(version);

        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String code = reader.lines().collect(Collectors.joining("\n"));

        JsonCreator jsonCreator = new JsonCreator();
        JsonObject json = jsonCreator.getJsonFromInputStream(config);
        JsonObject configJson = new CheckAdapter().adapt(json);

        RunnerResult.Analyze lintResult = runner.analyze(code, configJson);
        lintResult.getWarningsList().forEach(handler::reportError);
    }
}
