package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class CustomerUser extends User {
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long customerId;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "owner")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "customer")
    private List<EmployeeReview> reviews;
}
