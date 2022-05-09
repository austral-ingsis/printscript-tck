package implementation;

import interpreter.CustomInterpreter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new CustomInterpreter();
//         your PrintScript implementation should be returned here.
//         make sure to ADAPT your implementation to PrintScriptInterpreter interface.
//        throw new NotImplementedException("Needs implementation");


    }
}