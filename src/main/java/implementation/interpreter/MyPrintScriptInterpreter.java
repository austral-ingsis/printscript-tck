package implementation.interpreter;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.Runner;
import org.RunnerResult;

import java.io.*;
import java.util.stream.Collectors;

import static implementation.InputStreamToStringReader.convert;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Reader reader = convert(src);
        Runner runner = new Runner(version, reader);

        CustomInputProvider inputProvider = new CustomInputProvider(provider);
        CustomPrinter customPrinter = new CustomPrinter(emitter);

        RunnerResult.Execute result = runner.execute(version, customPrinter, inputProvider);
        result.getErrorsList().forEach(handler::reportError);
    }
}
