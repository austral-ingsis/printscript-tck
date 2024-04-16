package implementation;

import interpreter.InputProvider;
import interpreter.readInputFunction.ReadInputFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputProviderAdapter implements ReadInputFunction {

    private final InputProvider provider;

    public InputProviderAdapter(InputProvider provider) {
        this.provider = provider;
    }
    @Nullable
    @Override
    public String read(@NotNull String s) {
        return provider.input(s);
    }
}
