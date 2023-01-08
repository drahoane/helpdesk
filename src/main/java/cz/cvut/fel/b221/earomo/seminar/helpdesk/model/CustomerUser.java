package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class CustomerUser extends User {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "customer")
    private Set<EmployeeReview> reviews;


    @Override
    public UserType getUserType() {
        return UserType.CUSTOMER;
    }
}
