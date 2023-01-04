package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state.AwaitingResponseTicketState;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state.OpenTicketState;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state.ResolvedTicketState;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.state.TicketState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Long ticketId;

    @ManyToOne
    private CustomerUser owner;

    @ManyToMany(mappedBy = "assignedTickets")
    private Set<EmployeeUser> assignedEmployees;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "ticket")
    private Set<TicketMessage> messages;

    @OneToOne
    private EmployeeReview review;

    @OneToMany(mappedBy = "ticket")
    private Set<TimeRecord> timeRecords;

    @Transient
    private TicketState state;

    public TicketState getState() {
        if(state == null) {
            switch (status) {
                case OPEN -> state = new OpenTicketState();
                case AWAITING_RESPONSE -> state = new AwaitingResponseTicketState();
                case RESOLVED -> state = new ResolvedTicketState();
                default -> throw new RuntimeException("Undefined state");
            }

            state.setContext(this);
        }

        return state;
    }
}
