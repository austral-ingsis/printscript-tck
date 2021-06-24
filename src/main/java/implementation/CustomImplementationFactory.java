package implementation;

import interpreter.PrintScriptInterpreter;
import cli.CLI;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return ((src, version, emitter, handler) -> {
            try {
                CLI.execute(src, version, emitter::print);
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        });
    }
}
