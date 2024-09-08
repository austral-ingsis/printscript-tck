package interpreter;

import org.example.Interpreter;
import org.example.factory.InterpreterFactory;
import org.example.observer.BrokerObserver;
import org.example.observer.PrintBrokerObserver;

import java.io.InputStream;
import java.util.Scanner;

public class PullInterpreter implements PrintScriptInterpreter {
    private org.example.Interpreter createInterpreter(BrokerObserver<String> observer) {
        InterpreterFactory factory = new InterpreterFactory();
        factory.addObserver(observer);
        return factory.create();
    }

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if(!version.equals("1.0")) return;
        final PrintBrokerObserver observer = new PrintEmitterObserver(emitter);
        final org.example.Interpreter interpreter = createInterpreter(observer);

        new Scanner(src).useDelimiter("\\A");
        processLines(src, handler, interpreter);
    }

    private void processLines(InputStream src, ErrorHandler handler, Interpreter interpreter) {
        try {
            interpreter.interpret(new InterpreterIterator(src, handler));
        } catch (OutOfMemoryError e) {
            handler.reportError(e.getMessage());
        }
    }
}
