package implementation;

import com.printscript.interpreter.output.Output;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class OutputAdapter implements Output {
    private final PrintEmitter emitter;

    public OutputAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void write(@NotNull String s) {
        emitter.print(s);
    }
}
