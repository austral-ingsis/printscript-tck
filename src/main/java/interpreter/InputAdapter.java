package interpreter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputAdapter implements utils.InputProvider {
    private final InputProvider receiver;

    public InputAdapter(InputProvider receiver) {
        this.receiver = receiver;
    }

    @Nullable
    @Override
    public String readInput(@NotNull String s) {
        return receiver.input(s);
    }
}
