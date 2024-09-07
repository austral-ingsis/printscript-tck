package adapter;

import providers.inputProvider.InputProvider;

public class InputProviderAdapter implements InputProvider {
  private final interpreter.InputProvider inputProvider;

  public InputProviderAdapter(interpreter.InputProvider inputProvider) {
    this.inputProvider = inputProvider;
  }

  @Override
  public String getInput(String s) {
    return inputProvider.input(s);
  }
}
