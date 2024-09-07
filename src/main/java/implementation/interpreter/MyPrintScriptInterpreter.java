package implementation.interpreter;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.Runner;
import org.RunnerResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Runner runner = new Runner(version);
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String code = reader.lines().collect(Collectors.joining("\n"));

        CustomInputProvider inputProvider = new CustomInputProvider(provider);
        RunnerResult.Execute result = runner.execute(code, version, inputProvider);
        result.getPrintList().forEach(emitter::print);
        result.getErrorsList().forEach(handler::reportError);
    }
}
