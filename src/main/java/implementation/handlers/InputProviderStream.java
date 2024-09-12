package implementation.handlers;

import interpreter.InputProvider;

import java.io.InputStream;

public class InputProviderStream implements edu.handlers.expressions.InputProvider {
    private final InputProvider inputProvider;

    public InputProviderStream(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

  @Override
  public String input(String s) {
      System.out.println(s);
    return inputProvider.input(s);
  }
}