package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security.EmailAlreadyTakenException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketMessageRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class UserFactoryTest {
    @Autowired
    private TicketMessageRepository ticketMessageRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeUserRepository employeeUserRepository;
    @Autowired
    private UserMock userMock;
    @Autowired
    private TicketMock ticketMock;
    @Autowired
    private UserService userService;
    private UserFactory userFactory;

    @BeforeEach
    public void setUp() {
        ticketMessageRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        userMock.mock();
        ticketMock.mock();
        userFactory = new UserFactory();
    }

    @Test
    void createUserCreatesEmployeeUser() {
        EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("Arnold", "Černášunka", "arnold@sunka.cz", "test", UserType.EMPLOYEE);
        employeeUserRepository.save(employeeUser);

        List<EmployeeUser> employeeUserList = employeeUserRepository.findAll();

        assertTrue(employeeUserList.size() > 0);
        log.info(employeeUserList.get(0).getLastName());
    }
}
