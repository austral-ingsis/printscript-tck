package implementation;

import edu.austral.ingsis.CLI;
import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.File;

public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
        CLI.executeWithPrintAndErrorConsumers(src, version, emitter::print, handler::reportError);
    }
}
