package adapter;

import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;
import printscript.InputChannel;

public class AdaptedInputChannel implements InputChannel {
    private final InputProvider inputProvider;

    public AdaptedInputChannel(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    @NotNull
    @Override
    public String input(@NotNull String s) {
        return inputProvider.input(s);
    }
}
