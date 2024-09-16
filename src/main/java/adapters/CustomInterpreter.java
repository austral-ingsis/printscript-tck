package adapters;

import factories.Runner;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import utils.InterpreterResult;

import java.io.*;
import java.util.Iterator;
import java.util.Map;


public class CustomInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            System.gc();

            AdapterInputProvider inputProvider = new AdapterInputProvider(provider);
            Map<String, String> envMap = System.getenv();
            Runner runner = new Runner();
            Iterator<InterpreterResult> interpreterResults = runner.run(src, version, inputProvider, envMap, false).iterator();

            while (interpreterResults.hasNext()) {
                InterpreterResult result = interpreterResults.next();
                if (result.hasException()) {
                    handler.reportError(result.getException().getMessage());
                }
                if (result.hasPrintln()) {
                    emitter.print(result.getPrintln());
                }
            }
            System.gc();
        } catch (OutOfMemoryError | Exception e) {
            emitter = null;
            provider = null;
            src = null;
            version = null;
            handler.reportError(e.getMessage());
            System.gc();
        }
    }

}