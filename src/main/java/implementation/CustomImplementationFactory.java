package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

public class CustomImplementationFactory implements PrintScriptFactory {
    @Override
    public PrintScriptInterpreter interpreter() {
        return new Adapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        // your PrintScript formatter should be returned here.
        // make sure to ADAPT your formatter to PrintScriptFormatter interface.
        throw new NotImplementedException("Needs implementation");
    }

    @Override
    public PrintScriptLinter linter() {
        // your PrintScript linter should be returned here.
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        throw new NotImplementedException("Needs implementation");
    }
}
