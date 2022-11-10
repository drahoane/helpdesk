package cz.cvut.fel.b221.earomo.seminar.helpdesk;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class HelpDeskApplicationTests {

	private final EmployeeUserRepository employeeUserRepository;

	@Autowired
	HelpDeskApplicationTests(EmployeeUserRepository employeeUserRepository) {
		this.employeeUserRepository = employeeUserRepository;
	}

	@Test
	void userCreationTest() {
		UserFactory userFactory = new UserFactory();
		EmployeeUser employeeUser = (EmployeeUser) userFactory.createUser("Arnold", "Černášunka", "arnold@sunka.cz", "test", UserType.EMPLOYEE);
		employeeUserRepository.save(employeeUser);

		List<EmployeeUser> employeeUserList = employeeUserRepository.findAll();

		assertTrue(employeeUserList.size() > 0);
		log.info(employeeUserList.get(0).getLastName());
	}

}
