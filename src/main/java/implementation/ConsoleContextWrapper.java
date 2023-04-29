package implementation;

import interpreter.InputProvider;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;
import printscript.language.interpreter.contextProvider.ConsoleContext;
import printscript.language.interpreter.contextProvider.ContextProvider;
import printscript.language.interpreter.memory.Memory;

public class ConsoleContextWrapper implements ContextProvider {
    private final ConsoleContext consoleContext;
    private final InputProvider inputProvider;
    private final PrintEmitter printEmitter;
    public ConsoleContextWrapper(ConsoleContext consoleContext, PrintEmitter printEmitter, InputProvider inputProvider) {
        this.consoleContext = consoleContext;
        this.inputProvider = inputProvider;
        this.printEmitter = printEmitter;
    }
    @Override
    public void emit(@NotNull String s) {
        printEmitter.print(s);
        consoleContext.emit(s);
    }

    @NotNull
    @Override
    public Memory getMemory() {
        return consoleContext.getMemory();
    }

    @NotNull
    @Override
    public String read(@NotNull String s) {
        return consoleContext.read(inputProvider.input(s));
    }

    @Override
    public void setMemory(@NotNull Memory memory) {
        consoleContext.setMemory(memory);
    }
}
