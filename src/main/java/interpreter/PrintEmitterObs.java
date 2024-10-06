package interpreter;

import com.printscript.interpreter.results.InterpreterSuccess;

public class PrintEmitterObs {
  private final PrintEmitter emitter;

  public PrintEmitterObs(PrintEmitter emitter) {
    this.emitter = emitter;
  }

  public void notifyChange(InterpreterSuccess result) {
      emitter.print(result.getIntValue().toString());
  }
}
