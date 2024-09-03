package implementation.handlers;

import java.io.OutputStream;
import java.io.PrintStream;
import interpreter.ErrorHandler;

public class ErrorHandlerStream extends PrintStream {
  private final ErrorHandler errorHandler;

  public ErrorHandlerStream(OutputStream out, ErrorHandler errorHandler) {
    super(out);
    this.errorHandler = errorHandler;
  }

  @Override
  public void println(String x) {
    errorHandler.reportError(x);
  }

  @Override
  public void print(String x) {
    errorHandler.reportError(x);
  }
}
