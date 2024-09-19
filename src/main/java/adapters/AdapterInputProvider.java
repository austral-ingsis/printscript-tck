package adapters;

import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;
import utils.StringInputProvider;

public class AdapterInputProvider implements StringInputProvider {

    private final InputProvider inputProvider;

    public AdapterInputProvider(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    @NotNull
    @Override
    public String input(@NotNull String name) {
        return inputProvider.input(name);
    }
}
