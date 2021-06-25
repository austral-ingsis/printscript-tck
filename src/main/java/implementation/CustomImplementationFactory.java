package implementation;

import edu.austral.ingsis.interpreter.InterpreterRunner;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
//        throw new NotImplementedException("Needs implementation"); // TODO: implement
//        App cli = new App();
//        cli.run();
        // Dummy impl:
        return (src, version, emitter, handler) -> {
            try {
                InterpreterRunner.run(src.getAbsolutePath(), version, emitter::print);
            } catch (RuntimeException e) {
                handler.reportError(e.getMessage());
            }
        };
    }
}