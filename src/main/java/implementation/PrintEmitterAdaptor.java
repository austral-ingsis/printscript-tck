package implementation;

import edu.austral.ingsis.Printer;
import interpreter.PrintEmitter;

public class PrintEmitterAdaptor implements Printer {
  
  private final PrintEmitter printer;
  public PrintEmitterAdaptor(PrintEmitter p) {
    printer = p;
  }
  
  @Override
  public void print(String message) {
    printer.print(message.trim());
  }
}
