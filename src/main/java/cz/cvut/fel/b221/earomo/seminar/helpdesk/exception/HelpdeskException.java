package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

public class HelpdeskException extends RuntimeException {
    public HelpdeskException() {
        super();
    }

    public HelpdeskException(String message) {
        super(message);
    }

    public HelpdeskException(String message, Throwable cause) {
        super(message, cause);
    }

    public HelpdeskException(Throwable cause) {
        super(cause);
    }

    public HelpdeskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
