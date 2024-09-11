package implementation.interpreter;

import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;
import org.printers.Printer;

import java.util.List;

public class CustomPrinter implements Printer {

    private final PrintEmitter printer;

    public CustomPrinter(PrintEmitter printer) {
        this.printer = printer;
    }
    @Override
    public void print(@NotNull String s) {
        printer.print(s);
    }

    @NotNull
    @Override
    public List<String> getOutput() {
        return List.of();
    }
}
