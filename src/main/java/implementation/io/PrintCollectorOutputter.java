package implementation.io;

import interpreter.PrintEmitter;
import interpreter.output.Outputter;
import org.jetbrains.annotations.NotNull;

public class PrintCollectorOutputter implements Outputter {
    private final PrintEmitter printEmitter;
    public PrintCollectorOutputter(PrintEmitter printEmitter){
        this.printEmitter = printEmitter;
    }

    @Override
    public void output(@NotNull String s) {
        this.printEmitter.print(s);
    }
}
