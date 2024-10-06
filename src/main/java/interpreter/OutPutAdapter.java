package interpreter;

import org.jetbrains.annotations.NotNull;

public class OutPutAdapter implements com.printscript.interpreter.interfaces.OutPutProvider {
    private final PrintEmitter emitter;

    public OutPutAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

  @Override
  public void output(@NotNull String s) {
    emitter.print(s);
  }
}
