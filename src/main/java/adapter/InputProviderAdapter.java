package adapter;

import providers.inputprovider.InputProvider;

public class InputProviderAdapter implements InputProvider {
  private final interpreter.InputProvider inputProvider;
  private final PrintEmitterAdapter printEmitter;

  public InputProviderAdapter(interpreter.InputProvider inputProvider, PrintEmitterAdapter printEmitter) {
    this.inputProvider = inputProvider;
    this.printEmitter = printEmitter;
  }

  @Override
  public String getInput(String s) {
    printEmitter.print(s);
    return inputProvider.input(s);
  }
}
