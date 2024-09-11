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
import java.io.StringReader;
import java.util.stream.Collectors;

import static implementation.InputStreamToStringReader.convert;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        StringReader reader = convert(src);
        Runner runner = new Runner(version, reader);

        CustomInputProvider inputProvider = new CustomInputProvider(provider);

        RunnerResult.Execute result = runner.execute(version, inputProvider);
        result.getPrintList().forEach(emitter::print);
        result.getErrorsList().forEach(handler::reportError);
    }
}
