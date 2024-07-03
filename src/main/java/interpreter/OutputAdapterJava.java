package interpreter;

import emitter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class OutputAdapterJava implements PrintEmitter {
    private final interpreter.PrintEmitter emitter;

    public OutputAdapterJava(interpreter.PrintEmitter emitter){
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull String s){
        emitter.print(s);
    }
}
