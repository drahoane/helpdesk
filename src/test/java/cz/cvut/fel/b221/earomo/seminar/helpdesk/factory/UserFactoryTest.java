package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class UserFactoryTest {

    private final EmployeeUserRepository employeeUserRepository;
    private final UserMock userMock;


    @Autowired
    public UserFactoryTest(EmployeeUserRepository employeeUserRepository, UserMock userMock) {
        this.employeeUserRepository = employeeUserRepository;
        this.userMock = userMock;
    }


    @Test
    void createUserCreatesEmployeeUser() {
        UserFactory userFactory = new UserFactory();
        EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("Arnold", "Černášunka", "arnold@sunka.cz", "test", UserType.EMPLOYEE);
        employeeUserRepository.save(employeeUser);

        List<EmployeeUser> employeeUserList = employeeUserRepository.findAll();

        assertTrue(employeeUserList.size() > 0);
        log.info(employeeUserList.get(0).getLastName());
    }
}
