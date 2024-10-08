package interpreter;

import com.printscript.interpreter.interfaces.InputProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputProvAdapter implements InputProvider {
    private final interpreter.InputProvider inputProvider;

    public InputProvAdapter(interpreter.InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

  @Nullable
  @Override
  public String readInput(@NotNull String s) {
    return inputProvider.input(s);
  }
}
