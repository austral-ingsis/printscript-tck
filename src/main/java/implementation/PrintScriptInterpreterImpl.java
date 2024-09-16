package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.Runner;


import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            if (!version.equals("1.0") && !version.equals("1.1")) {
                handler.reportError("Unsupported version: " + version);
            }
            Runner runner = new Runner();
            Object results = runner.run(src.toString(), version);
            Iterator<Object> iterator = (Iterator<Object>) results;
            while (iterator.hasNext()) {
                Iterator<Object> result = (Iterator<Object>) iterator.next();
                if (result.hasNext()) {
                    emitter.print(result.toString());
                } else {
                    handler.reportError("Error: " + result);
                }
            }
        } catch (Exception | OutOfMemoryError e) {
            handler.reportError("Error: " + e.getMessage());
        }
    }
}
