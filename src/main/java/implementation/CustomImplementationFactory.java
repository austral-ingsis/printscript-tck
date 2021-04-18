package implementation;

import cli.App;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {

        return (src, version, emitter, handler) -> {
            try {
                App.run(src, version, emitter::print);
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        };
    }
}