package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
public class MockComponent {
    private final UserMock userMock;
    private final TicketMock ticketMock;

    @PostConstruct
    public void mock() {
        userMock.mock();
        //ticketMock.mock();
    }
}
