package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
@AllArgsConstructor
public class UserFactoryTest {

    private final EmployeeUserRepository employeeUserRepository;
    private final UserMock userMock;
    private final UserFactory userFactory;


    @Test
    void createUserCreatesEmployeeUser() {
        EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("Arnold", "Černášunka", "arnold@sunka.cz", "test", UserType.EMPLOYEE);
        employeeUserRepository.save(employeeUser);

        List<EmployeeUser> employeeUserList = employeeUserRepository.findAll();

        assertTrue(employeeUserList.size() > 0);
        log.info(employeeUserList.get(0).getLastName());
    }
}
