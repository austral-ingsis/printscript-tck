package implementation.interpreter;

import org.inputers.InputProvider;
import org.jetbrains.annotations.NotNull;

public class CustomInputProvider implements InputProvider {
    private final interpreter.InputProvider inputProvider;
    public CustomInputProvider(interpreter.InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    @NotNull
    @Override
    public String input() {
        return inputProvider.input("");
    }
}
