package util;

public class UnexpectedMessageException extends RuntimeException {
    public UnexpectedMessageException(String message) {
        super("Message: \"" + message + "\" was unexpected." );
    }
}
