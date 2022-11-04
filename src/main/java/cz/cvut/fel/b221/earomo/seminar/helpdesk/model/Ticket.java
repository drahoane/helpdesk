package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

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
    private Long ticketId;

    @ManyToOne
    private CustomerUser owner;

    @ManyToMany
    private Set<EmployeeUser> assignedEmployees;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @OneToMany(mappedBy = "ticket")
    private Set<TicketMessage> messages;
}
