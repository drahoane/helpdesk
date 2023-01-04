package cz.cvut.fel.b221.earomo.seminar.helpdesk.builder;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TicketBuilder {
    private CustomerUser owner;
    private final Set<EmployeeUser> assignedEmployees;
    private TicketStatus status;
    private TicketPriority priority;
    private Department department;
    private String title;
    private final Set<TimeRecord> timeRecords;

    public TicketBuilder() {
        this.status = TicketStatus.OPEN;
        this.priority = TicketPriority.MEDIUM;
        this.department = Department.PRODUCT_SUPPORT;
        this.assignedEmployees = new HashSet<>();
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

    /**
     * Default value: PRODUCT_SUPPORT
     */
    public TicketBuilder setDepartment(@NotNull Department department) {
        this.department = department;

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
        ticket.setDepartment(department);
        ticket.setTitle(title);
        ticket.setAssignedEmployees(assignedEmployees);
        ticket.setTimeRecords(timeRecords);
        ticket.setMessages(new HashSet<>());

        return ticket;
    }
}
