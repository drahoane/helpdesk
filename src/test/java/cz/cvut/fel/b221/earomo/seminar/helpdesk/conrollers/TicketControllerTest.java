package cz.cvut.fel.b221.earomo.seminar.helpdesk.conrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.builder.TicketBuilder;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.config.PasswordEncoderConfig;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.controller.TicketController;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.CreateTicketDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
public class TicketControllerTest extends BaseControllerForTesting {

    @MockBean
    private TicketService ticketService;
    @MockBean
    private TicketRepository ticketRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private EmployeeUserService employeeUserService;

    @Autowired
    private WebApplicationContext context;
    private ManagerUser mu;
    private EmployeeUser eu1;
    private CustomerUser cu1;
    private Ticket t1;
    private Ticket t2;
    private ObjectMapper objectMapper;
    private UserFactory userFactory;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userFactory = new UserFactory();
        mu = (ManagerUser)userFactory.createUser("K", "0", "k", "none", UserType.MANAGER);
        eu1 = (EmployeeUser)userFactory.createUser("A", "B", "C", "none", UserType.EMPLOYEE);
        cu1 = (CustomerUser)userFactory.createUser("D", "E", "F", "none", UserType.CUSTOMER);
        mu.setUserId(1L);
        eu1.setUserId(2L);
        cu1.setUserId(3L);

        userRepository.save(mu);
        userRepository.save(eu1);
        userRepository.save(cu1);

        TicketBuilder tb = new TicketBuilder();
        TicketMessage tm = new TicketMessage();
        tm.setSender(eu1);
        t1 = tb.setTitle("Ticket1")
                .setPriority(TicketPriority.LOW)
                .setStatus(TicketStatus.OPEN)
                .addMessage(tm)
                .setOwner(cu1)
                .assignEmployee(eu1)
                .build();
        ticketRepository.save(t1);

        t2 = tb.setTitle("Ticket2")
                .addMessage(tm)
                .setOwner(cu1)
                .assignEmployee(eu1)
                .build();
        ticketRepository.save(t2);
        userService = new UserService(userRepository, userFactory, passwordEncoder);
    }

    @Test
    public void mvcIsInitialized() {
        assertNotNull(mockMvc);
    }

    @Test
    public void findAllReturns401() throws Exception {
        when(ticketService.findAll()).thenReturn(Set.of(t1, t2));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ticket"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @WithMockUser
    public void findAllReturnsAllTickets() throws Exception {
        when(ticketService.findAll()).thenReturn(Set.of(t1, t2));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ticket"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    public void findAllReturnsAllUsers() throws Exception {
        //when(userService.findAll()).thenReturn(Set.of(mu, eu1, cu1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user"))
                //TODO
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    public void findReturnsTicketById() throws Exception {
        /*mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
         */
        when(ticketService.find(1L)).thenReturn(t1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ticket/1"))
                .andExpect(jsonPath("$.title").value("Ticket1"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    //@WithMockUser
    public void createTicket() throws Exception {
        /*mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();*/
        TicketBuilder tb = new TicketBuilder();
        TicketMessage tm = new TicketMessage();
        tm.setMessage("hi");
        Ticket t3 = tb.setTitle("Ticket3")
                .addMessage(tm)
                .setOwner(cu1)
                .setPriority(TicketPriority.MEDIUM)
                .assignEmployee(eu1)
                .build();
        ticketRepository.save(t2);
        when(ticketService.create(cu1, "Ticket3", "hi", TicketPriority.MEDIUM)).thenReturn(t3);
        assertEquals(cu1, userService.find(3L));
        CreateTicketDTO ticket = new CreateTicketDTO("testing create", "msg here", TicketPriority.MEDIUM);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ticket")
                        .with(httpBasic("F", "none")).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andDo(print())
                .andReturn();
    }

        //when(employeeDAO.addEmployee(any(Employee.class))).thenReturn(true);
        //final Set<Ticket> tickets = new HashSet<>(ticketRepository.findAll());
        //assertEquals(tickets, ticketServiceMock.findAll());
        //lenient().when(ticketServiceMock.findAll()).thenReturn(tickets);
        //MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ticket")).andReturn();
        //assertEquals(mvcResult.getResponse().getContentAsString(), ticketServiceMock.findAll());
}

