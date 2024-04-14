package implementation;

import interpreter.InputProvider;
import org.example.inputReader.InputReaderType;
import org.jetbrains.annotations.NotNull;

public class AdaptInput implements InputReaderType {
    InputProvider in;

    public AdaptInput (InputProvider in) {
        this.in = in;
    }

    @NotNull
//    @Override
    public String input(String name) {
        return in.input(name);
    }

    @NotNull
    @Override
    public String getInput() {
        return "";
    }
}
