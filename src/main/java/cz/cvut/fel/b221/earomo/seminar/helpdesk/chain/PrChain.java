package cz.cvut.fel.b221.earomo.seminar.helpdesk.chain;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ChainOfResponsibilityProcessingException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;

import java.util.Set;
import java.util.stream.Collectors;

public class PrChain extends TicketAssignmentChain {

    public PrChain(TicketService ticketService, EmployeeUserService employeeUserService) {
        super(ticketService, employeeUserService);
    }

    @Override
    public void assign(Ticket ticket) {
        if (!ticket.getDepartment().equals(Department.PR)) {
            if (getNext() == null)
                throw new ChainOfResponsibilityProcessingException("Next chain not found.");

            getNext().assign(ticket);
            return;
        }

        Set<EmployeeUser> availableEmployees = employeeUserService.getAllUnassignedEmployees()
                .stream()
                .filter(x -> x.getDepartment().equals(Department.PR))
                .collect(Collectors.toSet());

        ticketService.assignRandomEmployeeFromSet(ticket, availableEmployees);
    }
}
