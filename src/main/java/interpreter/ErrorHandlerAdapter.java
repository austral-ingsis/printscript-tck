package interpreter;

import runner.ErrorHandler;
import runner.Event;


public class ErrorHandlerAdapter implements ErrorHandler {
    private interpreter.ErrorHandler errorHandler;

    public ErrorHandlerAdapter(interpreter.ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    @Override
    public void handleError(Event error) {
        errorHandler.reportError(error.getMessage());
    }

}