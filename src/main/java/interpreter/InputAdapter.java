package interpreter;

import org.jetbrains.annotations.NotNull;
import utils.InputProvider;

public class InputAdapter implements InputProvider {
    private interpreter.InputProvider provider;

    public InputAdapter(interpreter.InputProvider provider) {
        this.provider = provider;
    }

    @NotNull
    @Override
    public String readInput(@NotNull String s) {
       return provider.input(s);
    }
}
