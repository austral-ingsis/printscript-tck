package implementation;

import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptInterpreterImpl;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new PrintScriptInterpreterImpl();
    }
}