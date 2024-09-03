package implementation.handlers;

import java.io.OutputStream;
import java.io.PrintStream;
import interpreter.PrintEmitter;

public class PrintEmitterStream extends PrintStream {
  private final PrintEmitter printEmitter;

  public PrintEmitterStream(OutputStream out, PrintEmitter printEmitter) {
    super(out);
    this.printEmitter = printEmitter;
  }

  @Override
  public void println(String x) {
    printEmitter.print(x);
  }

  @Override
  public void print(String x) {
    printEmitter.print(x);
  }
}

