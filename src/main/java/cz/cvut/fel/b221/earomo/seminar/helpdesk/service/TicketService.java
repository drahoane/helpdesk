package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceAlreadyExistsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.observer.Observer;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.observer.TicketMessageNotifier;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketMessageRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TicketService implements Observer {
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final TicketMessageRepository ticketMessageRepository;
    private final TicketMessageNotifier ticketMessageNotifier;

    @PostConstruct
    private void registerAsObserver() {
        ticketMessageNotifier.register(this);
    }

    @Transactional
    public Ticket create(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message,
                         @NotNull TicketPriority priority, @NotNull Department department) {
        Ticket ticket = ticketFactory.createTicket(customerUser, title, priority, department, this);

        TicketMessage ticketMessage = new TicketMessage();
        ticketMessage.setTicket(ticket);
        ticketMessage.setSender(customerUser);
        ticketMessage.setMessage(message);

        ticketMessageRepository.save(ticketMessage);

        ticket.getMessages().add(ticketMessage);
        ticketRepository.save(ticket);

        log.info("Ticket " + ticket.getTicketId() + " with title " + ticket.getTitle() + " has been created");

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
        if(ticketPriority != null) {
            ticket.setPriority(ticketPriority);
            log.info("Ticket's priority has been updated to " + ticket.getPriority());
        }
        // No permissions verification needed, it is already being verified by state machine
        if (ticketStatus != null) {
            ticket.getState().changeState(ticketStatus);
            log.info("Ticket's status has been updated to " + ticket.getStatus());
        }

        ticketRepository.save(ticket);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = ticketRepository.existsById(id);
        if (!exists) throw new ResourceNotFoundException(Ticket.class, id);

        ticketRepository.deleteById(id);
        log.info("Ticket " + id + " has been deleted");
    }

    @Transactional
    public void assignEmployee(@NotNull Ticket ticket, @NotNull EmployeeUser employee) {
        if (ticket.getAssignedEmployees().contains(employee))
            throw new ResourceAlreadyExistsException(EmployeeUser.class, employee.getUserId(), "assigned employees");
        ticket.getAssignedEmployees().add(employee);

        ticketRepository.save(ticket);
        log.info("Employee " + employee.getUserId() + " has been assigned to ticket " + ticket.getTicketId());
    }

    @Transactional
    public void unassignEmployee(@NotNull Long ticketId, @NotNull Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId));

        Set<EmployeeUser> newAssignedEmployees = ticket.getAssignedEmployees().stream().filter(e -> !Objects.equals(e.getUserId(), employeeId)).collect(Collectors.toSet());

        if (newAssignedEmployees.size() == ticket.getAssignedEmployees().size())
            throw new ResourceNotFoundException(EmployeeUser.class, employeeId, "assigned employees");

        ticket.setAssignedEmployees(newAssignedEmployees);
        ticketRepository.save(ticket);
        log.info("Employee " + employeeId + " has been unassigned from ticket " + ticketId);
    }

    @Transactional
    public void assignRandomEmployeeFromSet(@NotNull Ticket ticket, @NotNull Set<EmployeeUser> employees) {
        if (!employees.isEmpty()) {
            Random rand = new Random();
            int randElement = rand.nextInt(employees.size());
            int i = 0;

            for (EmployeeUser employee : employees) {
                if (i == randElement) {
                    assignEmployee(ticket, employee);
                    log.info("Employee " + employee.getUserId() + " has been assigned to ticket " + ticket.getTicketId() + " using random");
                    break;
                }

                i++;
            }
        }
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

        ticketMessageNotifier.newTicketMessage(ticketMessage);

        log.info("Message " + ticketMessage.getTicketMessageId() + " has been added to ticket " + ticketID);

        return ticketMessage;
    }

    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
        log.info("Ticket " + ticket.getTicketId() + " has been saved");
    }

    @Override
    @Transactional
    public void update(Object obj) {
        if(!(obj instanceof TicketMessage))
            throw new IllegalArgumentException();

        TicketMessage ticketMessage = (TicketMessage) obj;
        Ticket ticket = ticketMessage.getTicket();
        if(ticketMessage.getSender().getUserType().equals(UserType.CUSTOMER))
            ticket.getState().changeState(TicketStatus.OPEN);
        else
            ticket.getState().changeState(TicketStatus.AWAITING_RESPONSE);

        ticketRepository.save(ticket);
    }
}
