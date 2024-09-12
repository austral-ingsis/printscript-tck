package interpreter;

import org.jetbrains.annotations.NotNull;

public class PrintEmitterAdapter implements lib.PrintEmitter {
    private final PrintEmitter printEmitter;

    public PrintEmitterAdapter(PrintEmitter printEmitter) {
        this.printEmitter = printEmitter;
    }
    @Override
    public void print(@NotNull String message) {
        printEmitter.print(message);
    }
}
