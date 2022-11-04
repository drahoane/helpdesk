package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ManagerUser extends EmployeeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long managerId;

    @OneToOne
    private User user;
}
