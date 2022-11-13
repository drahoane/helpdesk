package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TicketFactory {
    private final EmployeeUserService employeeUserService;

    @Autowired
    public TicketFactory(EmployeeUserService employeeUserService) {
        this.employeeUserService = employeeUserService;
    }

    public Ticket createTicket(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message) {
        TicketBuilder ticketBuilder = new TicketBuilder();
        TicketMessage ticketMessage = new TicketMessage();

        ticketMessage.setMessage(message);
        ticketMessage.setSender(customerUser);

        ticketBuilder.setOwner(customerUser);
        ticketBuilder.setTitle(title);
        ticketBuilder.addMessage(ticketMessage);

        // FIXME
        Set<EmployeeUser> unassignedEmployees = employeeUserService.getAllUnassignedEmployees();
        if(unassignedEmployees.size() > 0) {
            ticketBuilder.assignEmployee(unassignedEmployees.iterator().next());
        }

        return ticketBuilder.build();
    }
}
