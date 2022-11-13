package cz.cvut.fel.b221.earomo.seminar.helpdesk;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.TicketFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.TicketMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.mock.UserMock;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class HelpDeskApplicationTests {

	// TODO: organize tests

	private final EmployeeUserRepository employeeUserRepository;
	private final CustomerUserRepository customerUserRepository;
	private final TicketMock ticketMock;
	private final UserMock userMock;
	private  final TicketRepository ticketRepository;
	private final TicketFactory ticketFactory;

	@Autowired
	HelpDeskApplicationTests(EmployeeUserRepository employeeUserRepository, CustomerUserRepository customerUserRepository, TicketMock ticketMock, UserMock userMock, TicketRepository ticketRepository, TicketFactory ticketFactory) {
		this.employeeUserRepository = employeeUserRepository;
		this.customerUserRepository = customerUserRepository;
		this.ticketMock = ticketMock;
		this.userMock = userMock;
		this.ticketRepository = ticketRepository;
		this.ticketFactory = ticketFactory;
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

	@Test
	void mockTest() {
		userMock.mock();
		ticketMock.mock();
		assertTrue(employeeUserRepository.findAll().size() > 0);
		assertTrue(ticketRepository.findAll().size() > 0);
	}

	@Test
	void ticketFactoryAutomaticAssignment() {
		userMock.mock();
		Ticket ticket = ticketFactory.createTicket(customerUserRepository.findById(1L).get(), "Ticket1", "bla");
		ticketRepository.save(ticket);

		assertEquals(1, ticket.getAssignedEmployees().size());
	}

	@Test
	void ticketFactoryMessageTest() {
		userMock.mock();
		Ticket ticket = ticketFactory.createTicket(customerUserRepository.findById(1L).get(), "Ticket1", "bla");
		ticketRepository.save(ticket);

		assertEquals("bla", ticket.getMessages().iterator().next().getMessage());
	}
}
