package implementation;

import interpreter.ConcreteInterpreter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {


    @Override
    public PrintScriptInterpreter interpreter() {
        return new ConcreteInterpreter();
    }
}