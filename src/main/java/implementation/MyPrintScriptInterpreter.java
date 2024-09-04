package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.Runner;

import java.io.InputStream;
import java.util.List;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        // Your implementation here
        Runner runner = new Runner(version);
        // Triple<List<String>, List<String>, List<String>> result =
        runner.execute(src.toString());
        /*
        for (String message : result.emitter()) {
            emitter.print(message);
        }
        for (String message : result.handler()) {
            handler.reportError(message);
        }
        for (String message : result.provider()) {
            provider.input(message);
        }
         */
    }
}
