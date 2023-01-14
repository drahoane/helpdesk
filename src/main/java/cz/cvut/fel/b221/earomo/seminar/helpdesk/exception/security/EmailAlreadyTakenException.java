package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.HelpdeskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class EmailAlreadyTakenException extends HelpdeskException {
    public EmailAlreadyTakenException() {
        super();
    }

    public EmailAlreadyTakenException(Class c, String action) {
        super("Already existing resource: " + c.getSimpleName() + " " + action.toUpperCase());
        log.info("EmailAlreadyTakenException has been thrown");
    }

    public EmailAlreadyTakenException(Class c, Long id, String action) {
        super("Already existing resource: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
        log.info("EmailAlreadyTakenException using id has been thrown");
    }
}
