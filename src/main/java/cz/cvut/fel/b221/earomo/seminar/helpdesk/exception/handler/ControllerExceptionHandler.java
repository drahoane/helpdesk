package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.handler;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security.EmailAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InsufficientPermissionsException.class)
    public ResponseEntity<ErrorHolder> insufficientPermissionsExceptionHandler(HelpdeskException e) {
        return new ResponseEntity<>(new ErrorHolder(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateChangeException.class)
    public ResponseEntity<ErrorHolder> illegalStateChangeExceptionHandler(HelpdeskException e) {
        ErrorHolder error = new ErrorHolder(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorHolder> resourceNotFoundExceptionHandler(HelpdeskException e) {
        return new ResponseEntity<>(new ErrorHolder(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorHolder> emailAlreadyTakenExceptionHandler(HelpdeskException e) {
        return new ResponseEntity<>(new ErrorHolder(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorHolder> resourceAlreadyExistsExceptionHandler(HelpdeskException e) {
        return new ResponseEntity<>(new ErrorHolder(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientRequestException.class)
    public ResponseEntity<ErrorHolder> insufficientRequestExceptionHanlder(HelpdeskException e) {
        return new ResponseEntity<>(new ErrorHolder(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
