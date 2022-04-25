package implementation;

import interpreter.PrintEmitter;

import java.util.function.Consumer;

class PrintEmitterAdapter implements Consumer<String> {

    PrintEmitter emitter;

    public PrintEmitterAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void accept(String s) {
        emitter.print(s);
    }

}
