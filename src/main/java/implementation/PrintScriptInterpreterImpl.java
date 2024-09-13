package implementation;

import java.io.InputStream;

import cli.CLIEnvProvider;
import interpreter.*;
import runner.Runner;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        final Runner runner = new Runner(version);

        final OutputAdapter outputAdapter = new OutputAdapter(emitter);
        final ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter(handler);
        final InputAdapter inputAdapter = new InputAdapter(provider);
        final CLIEnvProvider envProvider = new CLIEnvProvider();

        try{
            runner.execute(src, outputAdapter, errorHandlerAdapter, inputAdapter, envProvider);
        } catch (Exception | Error e) {
            errorHandlerAdapter.reportError(e.getMessage());
        }
    }
}
