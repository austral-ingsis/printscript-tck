package implementation;

import interpreter.InterpreterAdapter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new InterpreterAdapter();
    }
}
