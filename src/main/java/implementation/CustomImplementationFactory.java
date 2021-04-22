package implementation;

import edu.austral.ingsis.ConcreteInterpreter;
import edu.austral.ingsis.Interpreter;
import interpreter.PrintScriptInterpreter;

import java.io.File;

public class CustomImplementationFactory implements InterpreterFactory {

    private Interpreter interpreter;

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler) -> {
            try {
                interpreter = new ConcreteInterpreter("PrintScript " + version, new File("rules.txt"));
                interpreter.interpret(src, emitter::print);
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        };
    }
}
