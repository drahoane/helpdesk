package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;

@Component
@Lazy
public class TicketFactory {
    private final EmployeeUserService employeeUserService;

    public TicketFactory(EmployeeUserService employeeUserService) {
        this.employeeUserService = employeeUserService;
    }

    public Ticket createTicket(@NotNull CustomerUser customerUser, @NotNull String title, @NotNull String message,
                               @NotNull TicketPriority priority) {
        TicketBuilder ticketBuilder = new TicketBuilder();
        TicketMessage ticketMessage = new TicketMessage();

        ticketMessage.setMessage(message);
        ticketMessage.setSender(customerUser);

        ticketBuilder.setOwner(customerUser);
        ticketBuilder.setTitle(title);
        ticketBuilder.setPriority(priority);
        ticketBuilder.addMessage(ticketMessage);

        Set<EmployeeUser> unassignedEmployees = employeeUserService.getAllUnassignedEmployees();

        if(!unassignedEmployees.isEmpty()) {
            Random rand = new Random();
            int randElement = rand.nextInt(unassignedEmployees.size());
            int i = 0;

            for(EmployeeUser employee : unassignedEmployees) {
                if(i == randElement) {
                    ticketBuilder.assignEmployee(employee);
                    break;
                }

                i++;
            }
        }

        return ticketBuilder.build();
    }
}
