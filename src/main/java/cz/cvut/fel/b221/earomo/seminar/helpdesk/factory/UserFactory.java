package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public User createUser(String firstName, String lastName, String email, String password, UserType userType) {
        User user;
        switch(userType) {
            case CUSTOMER -> user = new CustomerUser();
            case EMPLOYEE -> user = new EmployeeUser();
            case MANAGER -> user = new ManagerUser();
            default -> throw new IllegalArgumentException();
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
