package implementation;

import formatter.PrintScriptFormatter;
import interpreter.InterpreterAdapter;
import interpreter.PrintScriptInterpreter;
import linter.LinterAdapter;
import linter.PrintScriptLinter;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return new InterpreterAdapter();

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }

    @Override
    public PrintScriptFormatter formatter() {
        // your PrintScript formatter should be returned here.
        // make sure to ADAPT your formatter to PrintScriptFormatter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement

        // Dummy impl: return (src, version, config, writer) -> { };
    }

    @Override
    public PrintScriptLinter linter() {
        return new LinterAdapter();
    }
}