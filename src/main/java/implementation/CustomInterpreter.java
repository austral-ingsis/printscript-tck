package implementation;

import implementation.io.InputProviderInputter;
import implementation.io.PrintCollectorOutputter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import interpreter.input.Inputter;
import interpreter.output.Outputter;
import printscript.v1.app.StreamedExecution;

import java.io.InputStream;

public class CustomInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Outputter out = new PrintCollectorOutputter(emitter);
        Inputter in = new InputProviderInputter(provider);
        try{
            new StreamedExecution(src, version, in, out).execute();
        } catch (Error e){
            handler.reportError(e.getMessage());
        } catch (Exception e){
            handler.reportError(e.getMessage());
        }


    }
}



