package cz.cvut.fel.b221.earomo.seminar.helpdesk.factory;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Lazy
public class UserFactory {
    private final PasswordEncoder passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(password));

        return user;
    }
}
