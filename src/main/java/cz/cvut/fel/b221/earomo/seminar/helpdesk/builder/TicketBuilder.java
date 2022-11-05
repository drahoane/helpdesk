package cz.cvut.fel.b221.earomo.seminar.helpdesk.builder;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketBuilder {
    private CustomerUser owner;
    private Set<EmployeeUser> assignedEmployees;
    private TicketStatus status;
    private TicketPriority priority;
    private Set<TicketMessage> ticketMessages;

    private Set<TimeRecord> timeRecords;

    public TicketBuilder() {
        this.status = TicketStatus.OPEN;
        this.priority = TicketPriority.MEDIUM;
        this.assignedEmployees = new HashSet<>();
        this.ticketMessages = new HashSet<>();
        this.timeRecords = new HashSet<>();
    }

    public TicketBuilder setOwner(CustomerUser owner) {
        this.owner = owner;

        return this;
    }

    /**
     * Default value: OPEN
     */
    public TicketBuilder setStatus(TicketStatus status) {
        this.status = status;

        return this;
    }

    /**
     * Default value: MEDIUM
     */
    public TicketBuilder setPriority(TicketPriority priority) {
        this.priority = priority;

        return this;
    }

    public TicketBuilder assignEmployee(EmployeeUser employee) {
        assignedEmployees.add(employee);

        return this;
    }

    public TicketBuilder addMessage(TicketMessage message) {
        ticketMessages.add(message);

        return this;
    }

    public TicketBuilder addMessages(Set<TicketMessage> messages) {
        ticketMessages.addAll(messages);

        return this;
    }

    public TicketBuilder addTimeRecord(TimeRecord timeRecord) {
        timeRecords.add(timeRecord);

        return this;
    }

    public TicketBuilder addTimeRecords(Set<TimeRecord> timeRecords) {
        timeRecords.addAll(timeRecords);

        return this;
    }

    public Ticket build() {
        if(owner == null)
            throw new NullPointerException("Ticket owner can not be null.");

        Ticket ticket = new Ticket();
        ticket.setOwner(owner);
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setAssignedEmployees(assignedEmployees);
        ticket.setMessages(ticketMessages);
        ticket.setTimeRecords(timeRecords);

        return ticket;
    }
}
