package implementation;

import com.printscript.interpreter.input.Input;
import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputAdapter implements Input {
    private final InputProvider provider;

    public InputAdapter(InputProvider provider) {
        this.provider = provider;
    }

    @NotNull
    @Override
    public String read(@Nullable String s) {
        return provider.input(s);
    }
}
