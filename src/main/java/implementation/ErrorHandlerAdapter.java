package implementation;

import interpreter.ErrorHandler;

import java.util.function.Consumer;

public class ErrorHandlerAdapter implements Consumer<String> {

    ErrorHandler handler;

    public ErrorHandlerAdapter(ErrorHandler handler) {
        this.handler = handler;
    }


    @Override
    public void accept(String s) {
        handler.reportError(s);
    }
}
