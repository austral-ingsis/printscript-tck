package adapters;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.main.Runner;
import utils.InterpreterResult;

import java.io.*;
import java.util.Iterator;


public class CustomInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Runner runner = new Runner();
            Iterator<InterpreterResult> interpreterResults = runner.run(src, version).iterator();
            while (interpreterResults.hasNext()) {
                InterpreterResult result = interpreterResults.next();
                if (result.hasException()) {
                    handler.reportError(result.getException().getMessage());
                }
                if (result.hasPrintln()) {
                    emitter.print(result.getPrintln());
                }
            }
        } catch (OutOfMemoryError | Exception e) {
            handler.reportError(e.getMessage());
        }

    }
}