package implementation;

import cli.CLI;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return (src, version, emitter, handler) -> {
            String[] args = new String[1];
            args[0] = src.getPath();
            try {
                CLI.main(args, version, emitter::print);
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        };
    }
}

