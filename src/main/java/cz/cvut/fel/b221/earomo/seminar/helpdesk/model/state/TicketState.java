package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegaleStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;

import java.lang.reflect.Field;

public abstract class TicketState {
    private Ticket context;

    public abstract void changeState(TicketStatus status) throws IllegaleStateChangeException;

    public void setContext(Ticket ticket) {
        this.context = ticket;
    }

    public Ticket getContext() {
        return this.context;
    }
}
