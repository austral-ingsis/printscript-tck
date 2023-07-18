package implementation;

import interpreter.PrintEmitter;
import org.austral.edu.Results.Result;

public class PrintEmitterAdapter implements Result {

    private PrintEmitter emitter;

    public PrintEmitterAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }
    @Override
    public void savePrintElement(String s) {
        emitter.print(s);
    }

    @Override
    public void saveReaderElement(String s) {
        emitter.print(s);
    }
}
