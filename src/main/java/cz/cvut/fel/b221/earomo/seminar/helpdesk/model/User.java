package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "\"user\"")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String password;

    private boolean accountDisabled = false;

    @OneToMany(mappedBy = "sender")
    private List<TicketMessage> ticketMessages;

    public boolean validatePassword(String password) {
        // TODO: Implement me

        return false;
    }

    public void setPassword(String password) {
        // TODO: Implement me
    }

    public UserType getUserType() {
        if(this instanceof CustomerUser)
            return UserType.CUSTOMER;

        if(this instanceof ManagerUser) // This has to be before EMPLOYEE because of inheritance
            return UserType.MANAGER;

        if(this instanceof EmployeeUser)
            return UserType.EMPLOYEE;

        throw new RuntimeException("Could not determine user type.");
    }
}
