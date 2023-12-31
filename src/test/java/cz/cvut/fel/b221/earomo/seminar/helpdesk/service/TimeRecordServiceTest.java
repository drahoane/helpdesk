package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TimeRecordServiceTest {

    @Autowired
    private TimeRecordService timeRecordService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketMock ticketMock;
    @Autowired
    private UserRepository userRepository;


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

        assertThrows(InsufficientPermissionsException.class, () ->
                timeRecordService.create(ticket.getTicketId())
        );
    }
}
