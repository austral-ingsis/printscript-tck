package adapter;

import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;
import printscript.PrintChannel;

public class AdaptedPrintChannel implements PrintChannel {
    private final PrintEmitter printEmitter;

    public AdaptedPrintChannel(PrintEmitter printEmitter) {
        this.printEmitter = printEmitter;
    }


    @Override
    public void print(@NotNull String s) {
        printEmitter.print(s);
    }
}
