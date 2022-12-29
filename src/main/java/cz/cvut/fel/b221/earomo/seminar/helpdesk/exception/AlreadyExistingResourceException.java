package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

public class AlreadyExistingResourceException extends RuntimeException {
    public AlreadyExistingResourceException() {
        super();
    }

    public AlreadyExistingResourceException(Class c, String action) {
        super("Already existing resource: " + c.getSimpleName() + " " + action.toUpperCase());
    }

    public AlreadyExistingResourceException(Class c, Long id, String action) {
        super("Already existing resource: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
    }
}
