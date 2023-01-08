package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class EmployeeUser extends User {
    @ManyToMany(mappedBy = "assignedEmployees", cascade = CascadeType.REMOVE)
    private Set<Ticket> assignedTickets;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Override
    public UserType getUserType() {
        return UserType.EMPLOYEE;
    }
}
