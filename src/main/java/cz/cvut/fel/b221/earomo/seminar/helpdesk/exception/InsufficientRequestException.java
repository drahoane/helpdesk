package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientRequestException extends HelpdeskException {

    public InsufficientRequestException() {
        super();
    }

    public InsufficientRequestException(Class c, String action) {
        super("Insufficient request: " + c.getSimpleName() + " " + action.toUpperCase());
        log.info("InsufficientRequestException has been thrown");
    }

    public InsufficientRequestException(Class c, Long id, String action) {
        super("Insufficient request: " + c.getSimpleName() + " #" + id + " " + action.toUpperCase());
        log.info("InsufficientRequestException using id has been thrown");
    }
}
