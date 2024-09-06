package implementation;

import com.printscript.interpreter.tracer.Tracer;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class TracerAdapter implements Tracer {
    private final PrintEmitter emitter;

    public TracerAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull String s) {
        emitter.print(s);
    }
}
