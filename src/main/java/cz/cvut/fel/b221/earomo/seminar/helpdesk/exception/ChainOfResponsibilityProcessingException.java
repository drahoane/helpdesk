package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

public class ChainOfResponsibilityProcessingException extends RuntimeException {
    public ChainOfResponsibilityProcessingException() {
    }

    public ChainOfResponsibilityProcessingException(String message) {
        super(message);
    }

    public ChainOfResponsibilityProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainOfResponsibilityProcessingException(Throwable cause) {
        super(cause);
    }

    public ChainOfResponsibilityProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
