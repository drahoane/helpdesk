package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long ticketId;

    @ManyToOne
    private CustomerUser owner;

    @ManyToMany
    private List<EmployeeUser> assignedEmployees;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @OneToMany(mappedBy = "ticket")
    private List<TicketMessage> messages;

    @OneToOne
    private EmployeeReview review;

    @OneToMany(mappedBy = "ticket")
    private List<TimeRecord> timeRecords;
}
