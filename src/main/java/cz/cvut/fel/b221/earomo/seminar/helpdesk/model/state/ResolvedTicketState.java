package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegaleStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;

public class ResolvedTicketState extends TicketState {
    @Override
    public void changeState(TicketStatus status) throws IllegaleStateChangeException {
        //
    }
}
