package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegalStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;

public class AwaitingResponseTicketState extends TicketState {
    @Override
    public void changeState(TicketStatus newStatus) throws IllegalStateChangeException {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;

        TicketState newState;

        if(newStatus == TicketStatus.RESOLVED) {
            if(securityUser.isCustomer() && !securityUser.ownsTicket(getContext()) ||
                    securityUser.isEmployee() && !securityUser.isAssignedToTicket(getContext())) {
                throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), ResolvedTicketState.class);
            }

            newState = new ResolvedTicketState();
        } else if(newStatus.equals(TicketStatus.OPEN)) {
            if(!securityUser.isEmployee() || securityUser.isCustomer() && !securityUser.ownsTicket(getContext())) {
                throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), OpenTicketState.class);
            }

            newState = new OpenTicketState();
        }
        else if(newStatus.equals(TicketStatus.AWAITING_RESPONSE)) {
            throw new IllegalStateChangeException("This state is already set.");
        } else {
            throw new IllegalStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass());
        }

        newState.setContext(getContext());
        getContext().setState(newState);
        getContext().setStatus(newStatus);
    }
}
