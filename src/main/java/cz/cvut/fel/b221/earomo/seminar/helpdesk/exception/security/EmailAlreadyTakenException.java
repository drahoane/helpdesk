package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.HelpdeskException;

//@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyTakenException extends HelpdeskException {
    public EmailAlreadyTakenException() {
        super();
    }

    public EmailAlreadyTakenException(Class c, String action) {
        super("Already existing resource: " + c.getSimpleName() + " " + action.toUpperCase());
    }

    public EmailAlreadyTakenException(Class c, Long id, String action) {
        super("Already existing resource: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
    }
}
