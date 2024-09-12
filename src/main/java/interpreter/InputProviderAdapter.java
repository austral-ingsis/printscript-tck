package interpreter;

import lib.InputProvider;
import org.jetbrains.annotations.NotNull;

public class InputProviderAdapter implements InputProvider {
    private final interpreter.InputProvider inputProvider;
    private final PrintEmitter printEmitter;

    public InputProviderAdapter(interpreter.InputProvider inputProvider, PrintEmitter printEmitter) {
        this.inputProvider = inputProvider;
        this.printEmitter = printEmitter;
    }

    @NotNull
    @Override
    public String input(@NotNull String name) {
        printEmitter.print(name);
        return inputProvider.input(name);
    }
}
