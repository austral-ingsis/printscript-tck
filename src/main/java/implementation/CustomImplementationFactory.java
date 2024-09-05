package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

import java.io.BufferedInputStream;
import java.util.Arrays;

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
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement
    }
}