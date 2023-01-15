package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketFactoryTest {
    @Autowired
    private TicketFactory ticketFactory;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        userMock.mock();
    }

    @Test
    public void createTicketCreatesTicket() {
        Ticket ticket = ticketFactory.createTicket(customerUserRepository.findAll().get(0), "Ticket1", TicketPriority.LOW, Department.SALES, ticketService);

        assertEquals("Ticket1", ticket.getTitle());
        assertEquals(TicketPriority.LOW, ticket.getPriority());
        assertEquals(Department.SALES, ticket.getDepartment());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
    }
}
