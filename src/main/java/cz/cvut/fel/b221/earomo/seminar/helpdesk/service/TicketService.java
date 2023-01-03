package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketUpdateDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketMessageRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final TicketMessageRepository ticketMessageRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketFactory ticketFactory,
                         TicketMessageRepository ticketMessageRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketFactory = ticketFactory;
        this.ticketMessageRepository = ticketMessageRepository;
    }

    @Transactional
    public Ticket create(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message,
                         @NotNull TicketPriority priority) {
        Ticket ticket = ticketFactory.createTicket(customerUser, title, message, priority);
        ticketRepository.save(ticket);

        return ticket;
    }

    @Transactional(readOnly = true)
    public Set<Ticket> findAll() {
        return new HashSet<>(ticketRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Set<Ticket> findAllByStatus(TicketStatus status) {
        return ticketRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public Ticket find(@NotNull Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, id));
    }

    @Transactional
    public void update(@NotNull Long ticketId, TicketPriority ticketPriority, TicketStatus ticketStatus) {
        Ticket ticket = find(ticketId);
        if(ticketPriority != null)
            ticket.setPriority(ticketPriority);

        // No permissions verification needed, it is already being verified by state machine
        if(ticketStatus != null) {
            ticket.getState().changeState(ticketStatus);
        }

        ticketRepository.save(ticket);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = ticketRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException(Ticket.class, id);

        ticketRepository.deleteById(id);
    }

    @Transactional
    public void assignEmployee(@NotNull Ticket ticket, @NotNull EmployeeUser employee) {
        ticket.getAssignedEmployees().add(employee);

        ticketRepository.save(ticket);
    }

    @Transactional
    public TicketMessage addTicketMessage(@NotNull User sender, @NotNull Long ticketID, @NotNull String message) {
        Ticket ticket = find(ticketID);
        TicketMessage ticketMessage = new TicketMessage();
        ticketMessage.setSender(sender);
        ticketMessage.setTicket(ticket);
        ticketMessage.setMessage(message);
        ticketMessage.setCreatedAt(LocalDateTime.now());
        ticket.getMessages().add(ticketMessage);

        ticketMessageRepository.save(ticketMessage);
        ticketRepository.save(ticket);

        return ticketMessage;
    }
}
