package interpreter;

import org.example.factory.InterpreterFactory;
import org.example.observer.BrokerObserver;
import org.example.observer.PrintBrokerObserver;

import java.io.InputStream;
import java.util.Scanner;

public class PullInterpreter implements PrintScriptInterpreter{

    private final PrintBrokerObserver observer = new PrintBrokerObserver();
    private final org.example.Interpreter interpreter = createInterpreter(observer);

    private org.example.Interpreter createInterpreter(BrokerObserver<String> observer) {
        InterpreterFactory factory = new InterpreterFactory();
        factory.addObserver(observer);
        return factory.create();
    }

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if(!version.equals("1.0")) return;

        new Scanner(src).useDelimiter("\\A");
        processLines(src, emitter, handler);
    }

    private void processLines(InputStream src, PrintEmitter emitter, ErrorHandler handler) {
        interpreter.interpret(new InterpreterIterator(src, handler));
        emitter.print(observer.getPrintedOutput());
    }
}
