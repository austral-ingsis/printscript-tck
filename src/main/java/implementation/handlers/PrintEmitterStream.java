package implementation.handlers;

import interpreter.PrintEmitter;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintEmitterStream extends PrintStream {
    private final PrintEmitter emitter;

    public PrintEmitterStream(PrintEmitter emitter) {
        super(OutputStream.nullOutputStream());
        this.emitter = emitter;
    }

    @Override
    public void println(String x) {
        emitter.print(x);
    }
}