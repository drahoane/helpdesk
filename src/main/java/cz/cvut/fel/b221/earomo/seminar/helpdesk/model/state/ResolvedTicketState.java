package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegalStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;

public class ResolvedTicketState extends TicketState {
    @Override
    public void changeState(TicketStatus newStatus) throws IllegalStateChangeException {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;

        TicketState newState;

        if(newStatus == TicketStatus.RESOLVED) {
            throw new IllegalStateChangeException("This state is already set.");
        } else if(newStatus.equals(TicketStatus.AWAITING_RESPONSE)) {
            if(!securityUser.isManager()) {
                throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), AwaitingResponseTicketState.class);
            }

            newState = new AwaitingResponseTicketState();
        }
        else if(newStatus.equals(TicketStatus.OPEN)) {
            if(!securityUser.isManager()) {
                throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), OpenTicketState.class);
            }

            newState = new OpenTicketState();
        } else {
            throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass());
        }

        newState.setContext(getContext());
        getContext().setState(newState);
        getContext().setStatus(newStatus);
    }
}
