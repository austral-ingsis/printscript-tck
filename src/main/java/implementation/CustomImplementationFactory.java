package implementation;

import adapters.CustomFormatter;
import adapters.CustomInterpreter;
import adapters.CustomLinter;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new CustomInterpreter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new CustomFormatter();
    }

    @Override
    public PrintScriptLinter linter() {
        return new CustomLinter();
    }
}