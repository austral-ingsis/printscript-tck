package interpreter;

import runner.Observer;
import runner.Runner;

import java.io.InputStream;
import java.util.List;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     *
     * @param src      Source file.
     * @param version  PrintScript version, 1.0 and 1.1 must be supported.
     * @param emitter  interface where print statements must be called.
     * @param handler  interface where all syntax and semantic error will be reported.
     * @param provider interface that provides input values during the execution.
     */
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        List<Observer> emptyList = List.of();
        Runner runner = new Runner(emptyList);
        ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter(handler);
        PrintEmitterAdapter printEmitterAdapter = new PrintEmitterAdapter(emitter);
        InputProviderAdapter inputProviderAdapter = new InputProviderAdapter(provider, emitter);
        try {
            runner.runExecute(src, version, errorHandlerAdapter, printEmitterAdapter, inputProviderAdapter);
        } catch (OutOfMemoryError e) {
            handler.reportError("Java heap space");
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
