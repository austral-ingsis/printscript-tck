package interpreter;

import org.jetbrains.annotations.NotNull;
import runner.ErrorHandler;

public class ErrorHandlerAdapter implements ErrorHandler {
    private final interpreter.ErrorHandler errorHandler;

    public ErrorHandlerAdapter(interpreter.ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void handleError(@NotNull String s) {
        errorHandler.reportError(s);
    }
}