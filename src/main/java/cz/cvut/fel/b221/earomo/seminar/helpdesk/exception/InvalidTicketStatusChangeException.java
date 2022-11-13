package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

public class InvalidTicketStatusChangeException extends RuntimeException {
    public InvalidTicketStatusChangeException() {
        super();
    }

    public InvalidTicketStatusChangeException(String s) {
        super(s);
    }
}
