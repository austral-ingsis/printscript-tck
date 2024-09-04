package implementation;

import formatter.AdaptedFormatter;
import interpreter.AdaptedInterpreter;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;
import linter.AdaptedLinter;


public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new AdaptedInterpreter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new AdaptedFormatter();
    }

    @Override
    public PrintScriptLinter linter() {
        return new AdaptedLinter();
    }
}