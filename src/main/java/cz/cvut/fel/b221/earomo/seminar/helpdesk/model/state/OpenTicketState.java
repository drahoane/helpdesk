package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegaleStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;

public class OpenTicketState extends TicketState {
    @Override
    public void changeState(TicketStatus newStatus) throws IllegaleStateChangeException {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;

        TicketState newState;

        if(newStatus == TicketStatus.RESOLVED) {
            if(securityUser.isCustomer() && !securityUser.ownsTicket(getContext()) ||
                    securityUser.isEmployee() && !securityUser.isAssignedToTicket(getContext())) {
                throw new IllegaleStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), ResolvedTicketState.class);
            }

            newState = new ResolvedTicketState();
        } else if(newStatus.equals(TicketStatus.AWAITING_RESPONSE)) {
            if(securityUser.isCustomer() || securityUser.isEmployee() && !securityUser.isAssignedToTicket(getContext())) {
                throw new IllegaleStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass(), AwaitingResponseTicketState.class);
            }

            newState = new AwaitingResponseTicketState();
        }
        else if(newStatus.equals(TicketStatus.OPEN)) {
            throw new IllegaleStateChangeException("This state is already set.");
        } else {
            throw new IllegaleStateChangeException(Ticket.class, getContext().getTicketId(), this.getClass());
        }

        newState.setContext(getContext());
        getContext().setState(newState);
        getContext().setStatus(newStatus);
    }
}
