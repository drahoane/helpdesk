package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.IllegalStateChangeException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.*;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketMock ticketMock;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EmployeeUserRepository employeeUserRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private TicketMessageRepository ticketMessageRepository;
    @Autowired
    private TimeRecordRepository timeRecordRepository;
    @Autowired
    private UserRepository userRepository;


    public void setUp() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        userMock.mock();
        ticketMock.mock();
    }

    @Test
    public void createReturnsTicket() {
        // Arrange
        setUp();
        CustomerUser customerUser = customerUserRepository.findAll().get(0);
        // Act
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        // Assert
        assertEquals("Test title", ticket.getTitle());
        assertEquals(1, ticket.getMessages().size());
        assertEquals("Test message", ticket.getMessages().iterator().next().getMessage());
        assertEquals(TicketPriority.HIGH, ticket.getPriority());
        assertEquals(Department.SALES, ticket.getDepartment());
        assertEquals(customerUser.getUserId(), ticket.getOwner().getUserId());
    }

    @Test
    public void assignRandomEmployeeFromSetAssignEmployeeToTicket() {
        // Arrange
        setUp();
        CustomerUser customerUser = customerUserRepository.findAll().get(0);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        // Act
        ticketService.assignRandomEmployeeFromSet(ticket, new HashSet<>(employeeUserRepository.findAll()));
        // Assert
        assertEquals(2, ticket.getAssignedEmployees().size());
    }

    @Test
    public void createAssignsEmployeeBasedOnDepartmentWhenEmployeeHasNoAssignedTickets() {
        // Arrange
        UserFactory userFactory = new UserFactory();
        employeeUserRepository.deleteAll();
        // Act
        CustomerUser customerUser = (CustomerUser)userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        EmployeeUser employeeUser = (EmployeeUser)userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE, Department.SALES);
        employeeUserRepository.save(employeeUser);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        ticketRepository.save(ticket);
        // Assert
        assertEquals(employeeUser.getUserId(), ticket.getAssignedEmployees().iterator().next().getUserId());
    }

    @Test
    public void createDoesNotAssignEmployeeBasedOnDepartmentWhenNoEmployeeIsInTheDepartment() {
        // Arrange
        UserFactory userFactory = new UserFactory();
        employeeUserRepository.deleteAll();
        // Act
        CustomerUser customerUser = (CustomerUser)userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        EmployeeUser employeeUser = (EmployeeUser)userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE, Department.PR);
        employeeUserRepository.save(employeeUser);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        ticketRepository.save(ticket);
        // Assert
        assertEquals(0, ticket.getAssignedEmployees().size());
    }

    @Test
    @WithUserDetails("peter@tee.com")  //manager
    public void changeStateChangesStateToOpen() {
        //Arrange
        UserFactory userFactory = new UserFactory();
        setUp();
        CustomerUser customerUser = (CustomerUser)userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        //Act
        ticketService.update(ticket.getTicketId(), null, TicketStatus.RESOLVED);
        //Assert
        assertEquals(TicketStatus.RESOLVED, ticket.getStatus());
    }

    @Test
    @WithUserDetails("alan@black.com")  //customer
    public void changeStateToAwaitingResponseAsCustomerThrowsException() {
        //Arrange
        UserFactory userFactory = new UserFactory();
        setUp();
        CustomerUser customerUser = (CustomerUser)userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        //Assert
        assertThrows(IllegalStateChangeException.class,() ->
                ticketService.update(ticket.getTicketId(), null, TicketStatus.RESOLVED));
    }
}