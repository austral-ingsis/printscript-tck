package implementation;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {

       return new PrintScriptAdapter();
    }
}