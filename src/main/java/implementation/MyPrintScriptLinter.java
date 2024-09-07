package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlinx.serialization.json.JsonObject;
import org.Runner;
import org.RunnerResult;

import java.io.IOException;
import java.io.InputStream;

public class MyPrintScriptLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Runner runner = new Runner(version);
        JsonObject configJson = new Utils().getJsonFromStream(config);

        RunnerResult.Analyze lintResult = runner.analyze(src.toString(), configJson);
        lintResult.getErrorsList().forEach(handler::reportError);
    }
}
