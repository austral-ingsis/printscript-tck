package implementation;

import interpreter.InputProvider;
import org.austral.edu.Results.Input;

public class InputProviderAdapter implements Input {

    private InputProvider inputProvider;

    public InputProviderAdapter(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }
    @Override
    public String input() {
        return inputProvider.input("");
    }
}
