package cz.cvut.fel.b221.earomo.seminar.helpdesk;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
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

	@Autowired
	public HelpDeskApplicationTests(EmployeeUserRepository employeeUserRepository, TicketMock ticketMock, UserMock userMock, TicketRepository ticketRepository) {
		this.employeeUserRepository = employeeUserRepository;
		this.ticketMock = ticketMock;
		this.userMock = userMock;
		this.ticketRepository = ticketRepository;
	}

	@Test
	void mockTest() {
		userMock.mock();
		ticketMock.mock();
		assertTrue(employeeUserRepository.findAll().size() > 0);
		assertTrue(ticketRepository.findAll().size() > 0);
	}
}
