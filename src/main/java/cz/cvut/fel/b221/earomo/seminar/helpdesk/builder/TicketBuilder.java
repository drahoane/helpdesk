package cz.cvut.fel.b221.earomo.seminar.helpdesk.builder;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TicketBuilder {
    private CustomerUser owner;
    private Set<EmployeeUser> assignedEmployees;
    private TicketStatus status;
    private TicketPriority priority;
    private String title;
    private Set<TicketMessage> ticketMessages;

    private Set<TimeRecord> timeRecords;

    public TicketBuilder() {
        this.status = TicketStatus.OPEN;
        this.priority = TicketPriority.MEDIUM;
        this.assignedEmployees = new HashSet<>();
        this.ticketMessages = new HashSet<>();
        this.timeRecords = new HashSet<>();
    }

    public TicketBuilder setOwner(@NotNull CustomerUser owner) {
        this.owner = owner;

        return this;
    }

    /**
     * Default value: OPEN
     */
    public TicketBuilder setStatus(@NotNull TicketStatus status) {
        this.status = status;

        return this;
    }

    /**
     * Default value: MEDIUM
     */
    public TicketBuilder setPriority(@NotNull TicketPriority priority) {
        this.priority = priority;

        return this;
    }

    public TicketBuilder setTitle(@NotNull String title) {
        this.title = title;

        return this;
    }

    public TicketBuilder assignEmployee(@NotNull EmployeeUser employee) {
        assignedEmployees.add(employee);

        return this;
    }

    public TicketBuilder addMessage(@NotNull TicketMessage message) {
        ticketMessages.add(message);

        return this;
    }

    public TicketBuilder addMessages(@NotNull Set<TicketMessage> messages) {
        ticketMessages.addAll(messages);

        return this;
    }

    public TicketBuilder addTimeRecord(@NotNull TimeRecord timeRecord) {
        timeRecords.add(timeRecord);

        return this;
    }

    public TicketBuilder addTimeRecords(@NotNull Set<TimeRecord> timeRecords) {
        timeRecords.addAll(timeRecords);

        return this;
    }

    public Ticket build() {
        if(owner == null)
            throw new NullPointerException("Ticket owner can not be null.");

        if(title == null)
            throw new NullPointerException("Ticket title can not be null.");

        Ticket ticket = new Ticket();
        ticket.setOwner(owner);
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setTitle(title);
        ticket.setAssignedEmployees(assignedEmployees);
        ticket.setMessages(ticketMessages);
        ticket.setTimeRecords(timeRecords);

        return ticket;
    }
}
