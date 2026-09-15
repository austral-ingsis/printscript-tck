package implementation;

import engine.EngineInputProvider;
import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;


public class InputProviderAdapter implements EngineInputProvider {

    private final InputProvider provider;
    private final PrintEmitterAdapter emitter;

    public InputProviderAdapter(PrintEmitterAdapter emmiter,InputProvider realProvider) {
        this.emitter = emmiter;
        this.provider = realProvider;
    }

    @NotNull
    @Override
    public String readInput(@NotNull String s) {
        emitter.print(s);
        return provider.input(s);
    }
}
