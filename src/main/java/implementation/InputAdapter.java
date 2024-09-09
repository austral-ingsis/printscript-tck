package implementation;

import com.printscript.interpreter.input.Input;
import com.printscript.interpreter.tracer.Tracer;
import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputAdapter implements Input {
    private final InputProvider provider;
    private final Tracer tracer;

    public InputAdapter(InputProvider provider, Tracer tracer) {
        this.provider = provider;
        this.tracer = tracer;
    }

    @NotNull
    @Override
    public String read(@Nullable String s) {
        tracer.print(s != null ? s : "");
        return provider.input(s);
    }
}
