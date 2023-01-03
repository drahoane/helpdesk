package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegalStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;

public abstract class TicketState {
    private Ticket context;

    public abstract void changeState(TicketStatus status) throws IllegalStateChangeException;

    public void setContext(Ticket ticket) {
        this.context = ticket;
    }

    public Ticket getContext() {
        return this.context;
    }
}
