package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

public class InsufficientRequestException extends HelpdeskException {

    public InsufficientRequestException() {
        super();
    }

    public InsufficientRequestException(Class c, String action) {
        super("Insufficient request: " + c.getSimpleName() + " " + action.toUpperCase());
    }

    public InsufficientRequestException(Class c, Long id, String action) {
        super("Insufficient request: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
    }
}
