package implementation;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {

        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }
}