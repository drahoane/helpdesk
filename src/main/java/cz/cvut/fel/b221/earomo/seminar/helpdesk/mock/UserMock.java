package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMock {
    private final CustomerUserRepository customerUserRepository;
    private final EmployeeUserRepository employeeUserRepository;
    private final ManagerUserRepository managerUserRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    public void mock() {
        CustomerUser cu1 = (CustomerUser) userFactory.createUser("Alan", "Black", "alan@black.com", passwordEncoder.encode("none"), UserType.CUSTOMER);
        CustomerUser cu2 = (CustomerUser) userFactory.createUser("Miriam", "Orange", "miriam@orange.com", passwordEncoder.encode("none"), UserType.CUSTOMER);
        EmployeeUser eu1 = (EmployeeUser) userFactory.createUser("John", "Smith", "john@smith.com", passwordEncoder.encode("none"), UserType.EMPLOYEE, Department.PRODUCT_SUPPORT);
        ManagerUser mu1 = (ManagerUser) userFactory.createUser("Peter", "Tee", "peter@tee.com", passwordEncoder.encode("none"), UserType.MANAGER, Department.SALES);

        customerUserRepository.save(cu1);
        customerUserRepository.save(cu2);
        employeeUserRepository.save(eu1);
        managerUserRepository.save(mu1);
    }
}
