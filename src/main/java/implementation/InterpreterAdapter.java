package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import java.io.InputStream;
import org.example.Runner;


public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        OurInputProvider inputProvider = new OurInputProvider(provider);
        OutPrintEmitter outputEmitter = new OutPrintEmitter(emitter);
        try {
            Runner.run(src, version, inputProvider, outputEmitter);
        }
        catch (OutOfMemoryError e){
            handler.reportError("Java heap space");
        }
        catch (Exception e){
            handler.reportError(e.toString());
        }
    }

}
