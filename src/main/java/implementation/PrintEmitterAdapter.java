package implementation;

import interpreter.DisplayMethod;
import interpreter.PrintEmitter;

public class PrintEmitterAdapter implements DisplayMethod {
    private final PrintEmitter emitter;

    public PrintEmitterAdapter(PrintEmitter emitter) {
        this.emitter=emitter;
    }

    @Override
    public void display(String text) {
        emitter.print(text);
    }
}
