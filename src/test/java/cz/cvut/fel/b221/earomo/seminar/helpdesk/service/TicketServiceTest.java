package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        setUp();
        CustomerUser customerUser = customerUserRepository.findAll().get(0);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);

        ticketService.assignRandomEmployeeFromSet(ticket, new HashSet<>(employeeUserRepository.findAll()));

        assertEquals(2, ticket.getAssignedEmployees().size());
    }

    @Test
    public void createAssignsEmployeeBasedOnDepartmentWhenEmployeeHasNoAssignedTickets() {
        // Arrange
        UserFactory userFactory = new UserFactory();
        employeeUserRepository.deleteAll();
        // Act
        CustomerUser customerUser = (CustomerUser) userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE, Department.SALES);
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
        CustomerUser customerUser = (CustomerUser) userFactory.createUser("Jane", "Black", "jane@black.com", "non", UserType.CUSTOMER);
        customerUserRepository.save(customerUser);
        EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE, Department.PR);
        employeeUserRepository.save(employeeUser);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.SALES);
        ticketRepository.save(ticket);
        // Assert
        assertEquals(0, ticket.getAssignedEmployees().size());
    }
}