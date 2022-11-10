package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket create(CustomerUser customerUser, String title, String message) {
        TicketFactory ticketFactory = new TicketFactory();

        Ticket ticket = ticketFactory.createTicket(customerUser, title, message);
        ticketRepository.save(ticket);

        return ticket;
    }

    public Set<Ticket> findAll() {
        return new HashSet<>(ticketRepository.findAll());
    }
}
