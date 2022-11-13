package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public Optional<Ticket> find(Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public void updateStatus(Long id, TicketStatus content) {
        Objects.requireNonNull(id);
        Optional<Ticket> ticketOptional = find(id);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            ticket.setStatus(content);
            ticketRepository.save(ticket);
        }
    }

    @Transactional
    public void updatePriority(Long id, TicketPriority content) {
        Ticket ticket = find(id);
        ticket.setPriority(content);
        ticketRepository.save(ticket);
    }


}
