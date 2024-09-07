package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new MyPrintScriptInterpreter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new MyPrintScriptFormatter();
    }

    @Override
    public PrintScriptLinter linter() {
        // your PrintScript linter should be returned here.
        return new MyPrintScriptLinter();
    }
}