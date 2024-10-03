package adapter;

import interpreter.PrintEmitter;
import providers.printprovider.PrintProvider;

public class PrintEmitterAdapter implements PrintProvider {

  private final PrintEmitter printEmitter;

  public PrintEmitterAdapter(PrintEmitter printEmitter) {

    this.printEmitter = printEmitter;
  }
  @Override
  public void print(String s) {
    printEmitter.print(s);
  }
}
