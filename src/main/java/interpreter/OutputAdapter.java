package interpreter;

import org.jetbrains.annotations.NotNull;
import utils.OutputProvider;

public class OutputAdapter implements OutputProvider {
    private final PrintEmitter emitter;

    public OutputAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull String s) {
        emitter.print(s);
    }
}
