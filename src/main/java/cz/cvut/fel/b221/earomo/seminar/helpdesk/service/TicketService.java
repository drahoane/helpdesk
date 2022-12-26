package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserTicketListDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketFactory ticketFactory) {
        this.ticketRepository = ticketRepository;
        this.ticketFactory = ticketFactory;
    }

    @Transactional
    public Ticket create(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message) {
        Ticket ticket = ticketFactory.createTicket(customerUser, title, message);
        ticketRepository.save(ticket);

        return ticket;
    }

    @Transactional(readOnly = true)
    public Set<Ticket> findAll() {
        return new HashSet<>(ticketRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> find(@NotNull Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public void updateStatus(@NotNull Long id, @NotNull TicketStatus status) {
        Ticket ticket = find(id).orElseThrow(IllegalArgumentException::new); // TODO: Maybe better exception?
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void updatePriority(@NotNull Long id, @NotNull TicketPriority priority) {
        Ticket ticket = find(id).orElseThrow(IllegalArgumentException::new);
        ticket.setPriority(priority);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        Ticket ticket = find(id).orElseThrow(IllegalArgumentException::new);
        ticketRepository.delete(ticket);
        //persist
    }
}
