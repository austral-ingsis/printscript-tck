package adapter;

import interpreter.ErrorHandler;
import org.jetbrains.annotations.NotNull;

public class AdaptedErrorHandler implements printscript.ErrorHandler {
    private final ErrorHandler errorHandler;

    public AdaptedErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void handleErrorMessage(@NotNull String s) {
        errorHandler.reportError(s);
    }
}
