package implementation;

import edu.austral.ingsis.Printer;
import interpreter.PrintEmitter;

public class PrintEmitterAdaptor implements Printer {

  private boolean integerMode = false;
  private final PrintEmitter printer;

  public PrintEmitterAdaptor(PrintEmitter p) {
    printer = p;
  }

  @Override
  public void print(String message) {
    if (integerMode) integerPrint(message);
    else printer.print(message);
  }

  private void integerPrint(String string) {
    try {
      int i = (int) Double.parseDouble(string);
      double d = Double.parseDouble(string);
      if (i == d)
        printer.print("" + i);
      else
        printer.print("" + d);
    } catch (NumberFormatException ignored) {
      printer.print(string);
    }
  }

  @Override
  public void setIntegerMode() {
    integerMode = true;
  }
}
