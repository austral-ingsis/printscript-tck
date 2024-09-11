package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.example.Runner;


public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        List<String> inputs = new ArrayList<>();
        try{
            inputs = Runner.run(src, version);
        }
        catch (Exception e){
            handler.reportError(e.toString());
        }
        for (String input : inputs) {
            emitter.print(input);
        }
    }

}
