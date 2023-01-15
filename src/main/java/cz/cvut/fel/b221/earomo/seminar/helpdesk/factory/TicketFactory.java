package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.chain.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@AllArgsConstructor
public class TicketFactory {
    private final EmployeeUserService employeeUserService;

    public Ticket createTicket(@NotNull CustomerUser customerUser, @NotNull String title,
                               @NotNull TicketPriority priority, Department department, TicketService ticketService) {
        TicketBuilder ticketBuilder = new TicketBuilder();

        ticketBuilder.setOwner(customerUser);
        ticketBuilder.setTitle(title);
        ticketBuilder.setPriority(priority);
        ticketBuilder.setDepartment(department);

        Ticket ticket = ticketBuilder.build();

        TicketAssignmentChain accounting = new AccountingChain(ticketService, employeeUserService);
        TicketAssignmentChain pr = new PrChain(ticketService, employeeUserService);
        TicketAssignmentChain productSupport = new ProductSupportChain(ticketService, employeeUserService);
        TicketAssignmentChain sales = new SalesChain(ticketService, employeeUserService);

        accounting.setNext(pr);
        pr.setNext(productSupport);
        productSupport.setNext(sales);

        accounting.assign(ticket);

        ticketService.save(ticket);

        return ticket;
    }
}
