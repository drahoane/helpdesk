package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class EmployeeUser extends User {
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long employeeId;

    @OneToOne
    private User user;

    // FIXME
    @ManyToMany
    private Set<Ticket> assignedTickets;
}
