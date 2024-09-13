package interpreter;

import org.jetbrains.annotations.NotNull;

public class ErrorHandlerAdapter implements utils.ErrorHandler {
    private final ErrorHandler errorHandler;

    public ErrorHandlerAdapter(ErrorHandler receiver) {
        this.errorHandler = receiver;
    }

    @Override
    public void reportError(@NotNull String s) {
        errorHandler.reportError(s);
    }
}
