package interpreter;

import runner.Runner;

import java.io.InputStream;
import java.util.LinkedList;

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
        try {
            Runner runner = new Runner(new LinkedList<>());
            ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter(handler);
            PrintEmitterAdapter printEmitterAdapter = new PrintEmitterAdapter(emitter);
            InputProviderAdapter inputProviderAdapter = new InputProviderAdapter(provider, emitter);
            runner.runExecute(src, version, errorHandlerAdapter, printEmitterAdapter, inputProviderAdapter);
        } catch (OutOfMemoryError e) {
            handler.reportError("Java heap space");
            System.out.println("Java heap space");
        } catch (Throwable e) {
            handler.reportError(e.getMessage());
        }
    }
}
