package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketMessageRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketFactoryTest {

    private final TicketFactory ticketFactory;
    private final CustomerUserRepository customerUserRepository;
    private final TicketRepository ticketRepository;
    private final TicketMessageRepository ticketMessageRepository;
    private final UserMock userMock;
    private final TicketService ticketService;

    Ticket ticket;


    @Autowired
    public TicketFactoryTest(TicketFactory ticketFactory, CustomerUserRepository customerUserRepository, TicketRepository ticketRepository, TicketMessageRepository ticketMessageRepository, UserMock userMock, TicketService ticketService) {
        this.ticketFactory = ticketFactory;
        this.customerUserRepository = customerUserRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMessageRepository = ticketMessageRepository;
        this.userMock = userMock;
        this.ticketService = ticketService;
    }

    @BeforeEach
    void setUp() {
        userMock.mock();
        ticket = ticketFactory.createTicket(customerUserRepository.findById(1L).get(), "Ticket1", TicketPriority.LOW, Department.SALES, ticketService);
    }

    @Test
    void assignEmployeeTicketIsAssignedToOneOfTheUnassignedEmployee() {
        assertEquals(1, ticket.getAssignedEmployees().size());
    }

    @Test
    void getMessageMessageIsSetWithinCreatingTicket() {
        assertEquals("first ticket", ticket.getMessages().iterator().next().getMessage());
    }
}
