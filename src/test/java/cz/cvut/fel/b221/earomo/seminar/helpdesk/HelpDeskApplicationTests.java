package cz.cvut.fel.b221.earomo.seminar.helpdesk;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelpDeskApplicationTests {

	private final EmployeeUserRepository employeeUserRepository;
	private final TicketMock ticketMock;
	private final UserMock userMock;
	private  final TicketRepository ticketRepository;
	private final ManagerUserRepository managerUserRepository;
	@Autowired
	private TicketMessageRepository ticketMessageRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	public HelpDeskApplicationTests(EmployeeUserRepository employeeUserRepository, TicketMock ticketMock, UserMock userMock, TicketRepository ticketRepository, ManagerUserRepository managerUserRepository) {
		this.employeeUserRepository = employeeUserRepository;
		this.ticketMock = ticketMock;
		this.userMock = userMock;
		this.ticketRepository = ticketRepository;
		this.managerUserRepository = managerUserRepository;
	}

	@Test
	void mockTest() {
		ticketMessageRepository.deleteAll();
		ticketRepository.deleteAll();
		userRepository.deleteAll();
		userMock.mock();
		ticketMock.mock();
		assertTrue(employeeUserRepository.findAll().size() > 0);
		assertTrue(ticketRepository.findAll().size() > 0);

		User user = (User) managerUserRepository.findAll().get(0);
		assertEquals(UserType.MANAGER, user.getUserType());
	}
}
