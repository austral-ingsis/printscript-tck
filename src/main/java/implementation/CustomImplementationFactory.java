package implementation;

import interpreter.PrintScriptInterpreterr;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreterr interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        // Dummy impl: return (src, version, emitter, handler) -> { };
        return new PrintScriptInterpreterImpl();
    }
}