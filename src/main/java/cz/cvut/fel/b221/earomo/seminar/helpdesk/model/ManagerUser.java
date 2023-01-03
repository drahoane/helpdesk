package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ManagerUser extends EmployeeUser {
    @Override
    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public UserType getUserType() {
        return UserType.MANAGER;
    }
}
