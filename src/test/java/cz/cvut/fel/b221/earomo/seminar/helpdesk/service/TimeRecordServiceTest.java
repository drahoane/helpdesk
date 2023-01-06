package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

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


    @BeforeEach
    public void setUp() {
        userMock.mock();
        ticketMock.mock();
    }

    @Test
    @WithMockUser
    public void createThrowsExceptionForUnassignedEmployee() {
        Ticket ticket = ticketRepository.findAll().get(0);
        EmployeeUser employeeUser = employeeUserRepository.findAll().get(0);

        TimeRecord timeRecord = timeRecordService.create(ticket.getTicketId(), employeeUser.getUserId());

        assertThrows(InsufficientPermissionsException.class,() -> {});
    }
}
