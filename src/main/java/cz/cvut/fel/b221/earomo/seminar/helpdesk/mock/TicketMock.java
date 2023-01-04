package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TicketMock {
    private final CustomerUserRepository customerUserRepository;
    private final EmployeeUserRepository employeeUserRepository;
    private final ManagerUserRepository managerUserRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketMock(CustomerUserRepository customerUserRepository, EmployeeUserRepository employeeUserRepository, ManagerUserRepository managerUserRepository, TicketRepository ticketRepository) {
        this.customerUserRepository = customerUserRepository;
        this.employeeUserRepository = employeeUserRepository;
        this.managerUserRepository = managerUserRepository;
        this.ticketRepository = ticketRepository;
    }

    public void mock() {
        TicketBuilder tb = new TicketBuilder();
        TicketMessage tm = new TicketMessage();
        Ticket t1 = tb.setTitle("Ticket1")
                .setOwner(customerUserRepository.findById(1L).get())
                .assignEmployee(employeeUserRepository.findById(3L).get())
                .build();
        Ticket t2 = tb.setTitle("Ticket2")
                .setOwner(customerUserRepository.findById(2L).get())
                .assignEmployee(employeeUserRepository.findById(3L).get())
                .assignEmployee(managerUserRepository.findById(4L).get())
                .build();

        ticketRepository.save(t1);
        ticketRepository.save(t2);
    }
}
