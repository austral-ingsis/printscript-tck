package implementation;

import edu.austral.ingsis.gradle.interpreter.util.InputReader;
import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;

public class InputReaderAdapter implements InputReader {
    private final InputProvider inputProvider;

    public InputReaderAdapter(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    @NotNull
    @Override
    public Object read() {
        return inputProvider;
    }
}
