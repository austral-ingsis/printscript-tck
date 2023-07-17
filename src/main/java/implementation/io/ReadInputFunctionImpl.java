package implementation.io;

import ingsis.printscript.interpreter.ReadInputFunction;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class ReadInputFunctionImpl implements ReadInputFunction {

    InputProvider provider;
    PrintEmitter emitter;

    public ReadInputFunctionImpl(InputProvider provider, PrintEmitter emitter) {
        this.provider = provider;
        this.emitter = emitter;
    }

    @NotNull
    @Override
    public String read(@NotNull String s) {
        this.emitter.print(s);
        return this.provider.input("");
    }
}
