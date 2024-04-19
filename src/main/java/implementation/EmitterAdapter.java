package implementation;

import edu.austral.ingsis.gradle.interpreter.util.Printer;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;


public class EmitterAdapter implements Printer {
    private final PrintEmitter emitter;

    public EmitterAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull String s) {
        emitter.print(s);
    }
}
