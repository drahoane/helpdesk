package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
@AllArgsConstructor
public class TicketFactory {
    private final EmployeeUserService employeeUserService;
    private final TicketService ticketService;

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

        Set<EmployeeUser> unassignedEmployees = employeeUserService.findAll();
        Set<EmployeeUser> assignedEmployees = new HashSet<>();
        Set<Ticket> unresolvedTickets = new HashSet<>();

        unresolvedTickets.addAll(ticketService.findAllByStatus(TicketStatus.OPEN));
        unresolvedTickets.addAll(ticketService.findAllByStatus(TicketStatus.AWAITING_RESPONSE));

        unresolvedTickets.stream().forEach(ticket -> assignedEmployees.addAll(ticket.getAssignedEmployees()));

        unassignedEmployees.removeAll(assignedEmployees);

        if(unassignedEmployees.size() > 0) {
            Random rand = new Random();
            int randElement = rand.nextInt(unassignedEmployees.size());
            int i = 0;

            for(EmployeeUser employee : unassignedEmployees) {
                if(i == randElement) {
                    ticketBuilder.assignEmployee(employee);
                }

                i++;
            }
        }

        return ticketBuilder.build();
    }
}
