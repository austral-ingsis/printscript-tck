package interpreter;

import com.printscript.interpreter.Interpreter;
import com.printscript.interpreter.interfaces.InterpreterResult;
import com.printscript.interpreter.results.InterpreterSuccess;

public class PrintEmitterObs {
  private final PrintEmitter emmiter;

  public PrintEmitterObs(PrintEmitter emmiter) {
    this.emmiter = emmiter;
  }

  public void notifyChange(InterpreterResult result) {
    if (result instanceof InterpreterSuccess) {
      emmiter.print(((InterpreterSuccess) result).getOriginalValue().toString().strip()); //!!USE .getIntValue() WHEN UPDATED VERSION
    }
  }
}
