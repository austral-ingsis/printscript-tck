package implementation;

import edu.austral.ingsis.PrintScript;
import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.File;

public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
        PrintScript.executeWithPrintAndErrorConsumers(new String[]{"-f " + src.getAbsolutePath()}, emitter::print, handler::reportError);
    }
}
