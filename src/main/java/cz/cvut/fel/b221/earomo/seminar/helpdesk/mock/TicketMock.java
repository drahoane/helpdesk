package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TicketMock {
    private final CustomerUserRepository customerUserRepository;
    private final TicketRepository ticketRepository;

    public void mock() {
        TicketBuilder tb = new TicketBuilder();
        TicketMessage tm = new TicketMessage();
        Ticket t1 = tb.setTitle("Ticket1")
                .setOwner(customerUserRepository.findAll().get(0))
                .build();
        Ticket t2 = tb.setTitle("Ticket2")
                .setOwner(customerUserRepository.findAll().get(1))
                .build();

        ticketRepository.save(t1);
        ticketRepository.save(t2);
    }
}
