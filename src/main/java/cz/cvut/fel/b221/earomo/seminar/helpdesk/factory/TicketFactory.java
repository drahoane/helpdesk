package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import org.jetbrains.annotations.NotNull;

public class TicketFactory {
    public Ticket createTicket(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message) {
        TicketBuilder ticketBuilder = new TicketBuilder();
        TicketMessage ticketMessage = new TicketMessage();

        ticketMessage.setMessage(message);
        ticketMessage.setSender(customerUser);

        ticketBuilder.setOwner(customerUser);
        ticketBuilder.setTitle(title);
        ticketBuilder.addMessage(ticketMessage);

        // TODO: Automatically assign new ticket to employee with no assigned tickets if such employee is found.

        return ticketBuilder.build();
    }
}
