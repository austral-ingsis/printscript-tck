package implementation;

import interpreter.DisplayMethod;
import interpreter.InputMethod;
import interpreter.InputProvider;

public class InputProviderAdapter implements InputMethod {
    private final InputProvider inputProvider;

    public InputProviderAdapter(InputProvider inputProvider) {
        this.inputProvider=inputProvider;
    }

    @Override
    public String readInput(String name, DisplayMethod displayMethod) {
        displayMethod.display(name);
        return inputProvider.input(name);
    }
}
