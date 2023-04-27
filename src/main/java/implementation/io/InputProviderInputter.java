package implementation.io;

import interpreter.InputProvider;
import interpreter.input.Inputter;
import org.jetbrains.annotations.Nullable;

public class InputProviderInputter implements Inputter {

    private final InputProvider inputProvider;

    public InputProviderInputter(InputProvider inputProvider){
        this.inputProvider = inputProvider;
    }

    @Nullable
    @Override
    public String getInputLine() {
        return this.inputProvider.input("");
    }
}