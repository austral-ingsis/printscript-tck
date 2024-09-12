package interpreter;

import org.example.Interpreter;
import org.example.Result;
import org.example.iterators.InterpreterIterator;
import org.example.observer.BrokerObserver;
import org.example.observer.Observer;
import org.example.observer.ObserverType;
import org.example.observer.PrintBrokerObserver;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PullInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
      final PrintBrokerObserver observer = new PrintEmitterObserver(emitter);
      final org.example.Interpreter interpreter = createInterpreter(observer, provider);

      new Scanner(src).useDelimiter("(?<=\\}|(?<!\\{[^{}]);(?![^{}]\\}))(?=(?!.else).*)");
      processLines(src, handler, interpreter, version);
    }

    private void processLines(InputStream src, ErrorHandler handler, org.example.Interpreter interpreter, String version) {
        try {
            InterpreterIterator iterator = new InterpreterIterator(new Scanner(src), "", version);
            Observer<Result> observer = new ErrorHandlerObserver(handler);
            iterator.addObserver(observer);
            interpreter.interpret(iterator);
        } catch (OutOfMemoryError e) {
            handler.reportError(e.getMessage());
        }
    }

  private Interpreter createInterpreter(BrokerObserver<String> observer, InputProvider provider) {
    return new org.example.PrintScriptInterpreter(Map.ofEntries(Map.entry(ObserverType.PRINTLN_OBSERVER, observer)), List.of(), new TckInputListener(provider));
  }

}
