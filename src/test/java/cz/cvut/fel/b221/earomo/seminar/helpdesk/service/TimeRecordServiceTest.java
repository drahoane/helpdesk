package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TimeRecordServiceTest {

    @Autowired
    private TimeRecordService timeRecordService;
    @Autowired
    private EmployeeUserRepository employeeUserRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketMock ticketMock;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private TicketService ticketService;


    @BeforeEach
    public void setUp() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        userMock.mock();
        ticketMock.mock();
    }

    @Test
    @WithUserDetails("john@smith.com")  //employee
    public void createThrowsExceptionForUnassignedEmployee() {
        Ticket ticket = ticketRepository.findAll().get(0);

        assertThrows(InsufficientPermissionsException.class,() ->
            timeRecordService.create(ticket.getTicketId())
        );
    }

    @Test
    @WithUserDetails("john@smith.com")  //employee
    public void createSecondCreateThrowsException() {
        setUp();
        CustomerUser customerUser = customerUserRepository.findAll().get(0);
        Ticket ticket = ticketService.create(customerUser, "Test title", "Test message", TicketPriority.HIGH, Department.PRODUCT_SUPPORT);
        TimeRecord tm = timeRecordService.create(ticket.getTicketId());
        assertThrows(ResponseStatusException.class,() ->
                timeRecordService.create(ticket.getTicketId()));
    }
}
