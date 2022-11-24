package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
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
    private final UserMock userMock;

    Ticket ticket;


    @Autowired
    public TicketFactoryTest(TicketFactory ticketFactory, CustomerUserRepository customerUserRepository, TicketRepository ticketRepository, UserMock userMock) {
        this.ticketFactory = ticketFactory;
        this.customerUserRepository = customerUserRepository;
        this.ticketRepository = ticketRepository;
        this.userMock = userMock;
    }

    @BeforeEach
    void setUp() {
        userMock.mock();
        ticket = ticketFactory.createTicket(customerUserRepository.findById(1L).get(), "Ticket1", "first ticket");
        ticketRepository.save(ticket);
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
