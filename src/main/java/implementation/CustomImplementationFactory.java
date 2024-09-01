package implementation;

import interpreter.AdaptedInterpreter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new AdaptedInterpreter();
    }
}