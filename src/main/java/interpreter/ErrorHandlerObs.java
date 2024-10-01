package interpreter;

import com.printscript.interpreter.interfaces.InterpreterResult;
import com.printscript.interpreter.results.InterpreterFailure;
import com.printscript.interpreter.results.InterpreterResultInformation;
import com.printscript.interpreter.results.InterpreterSuccess;

public class ErrorHandlerObs {
  private final ErrorHandler handler;
  public ErrorHandlerObs (ErrorHandler handler) {
    this.handler = handler;
  }

  public void notifyChange (InterpreterResult result) {
    if (result instanceof InterpreterFailure) {
      handler.reportError(((InterpreterFailure) result).getErrorMessage());
    }
  }
}
