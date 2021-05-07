package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import cli.CLI;

import java.io.File;

public class CustomInterpreter implements PrintScriptInterpreter {
    private final CLI cli;

    public CustomInterpreter() {
        cli = new CLI();
    }

    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
        try {
            cli.interpret(src, version, emitter::print);
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
