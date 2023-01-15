package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class InsufficientPermissionsException extends HelpdeskException {
    public InsufficientPermissionsException() {
        super();
    }

    public InsufficientPermissionsException(Class c, String action) {
        super("Insufficient permissions: " + c.getSimpleName() + " " + action.toUpperCase());
        log.info("InsufficientPermissionsException has been thrown");
    }

    public InsufficientPermissionsException(Class c, Long id, String action) {
        super("Insufficient permissions: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
        log.info("InsufficientPermissionsException using id has been thrown");
    }
}
