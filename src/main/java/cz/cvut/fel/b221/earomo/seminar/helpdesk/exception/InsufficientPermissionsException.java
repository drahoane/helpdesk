package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

//@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientPermissionsException extends HelpdeskException {
    public InsufficientPermissionsException() {
        super();
    }

    public InsufficientPermissionsException(Class c, String action) {
        super("Insufficient permissions: " + c.getSimpleName() + " " + action.toUpperCase());
    }

    public InsufficientPermissionsException(Class c, Long id, String action) {
        super("Insufficient permissions: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
    }
}
