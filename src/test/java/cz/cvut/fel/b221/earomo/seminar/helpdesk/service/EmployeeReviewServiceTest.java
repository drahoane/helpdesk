package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegalStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class EmployeeReviewServiceTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketMock ticketMock;
    @Autowired
    private EmployeeUserRepository employeeUserRepository;
    @Autowired
    private EmployeeReviewService employeeReviewService;
    @Autowired
    private CustomerUserRepository customerUserRepository;


    public void setUp() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        userMock.mock();
        ticketMock.mock();
    }

    @Test
    @WithUserDetails("alan@black.com")  //customer
    public void createReviewAsCustomerCreatesReview() {
        //Arrange
        setUp();
        Ticket ticket = ticketRepository.findAll().get(0);
        CustomerUser customerUser = customerUserRepository.findAll().get(0);
        //Act
        EmployeeReview er = employeeReviewService.create(ticket.getTicketId(), EmployeeReviewGrade.A, "Helped a lot", customerUser);
        //Assert
        assertEquals(EmployeeReviewGrade.A, er.getGrade());
        assertEquals("Helped a lot", er.getTextReview());
        assertEquals(customerUser, er.getCustomer());
    }
}
