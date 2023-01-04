package cz.cvut.fel.b221.earomo.seminar.helpdesk.chain;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;

public abstract class TicketAssignmentChain {

    private TicketAssignmentChain next;
    protected final TicketService ticketService;
    protected final EmployeeUserService employeeUserService;

    public TicketAssignmentChain(TicketService ticketService, EmployeeUserService employeeUserService) {
        this.ticketService = ticketService;
        this.employeeUserService = employeeUserService;
    }

    public void setNext(TicketAssignmentChain next) {
        this.next = next;
    }

    protected TicketAssignmentChain getNext() {
        return next;
    }

    public abstract void assign(Ticket ticket);

}
