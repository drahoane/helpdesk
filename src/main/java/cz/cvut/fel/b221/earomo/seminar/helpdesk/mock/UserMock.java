package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
public class UserMock {
    private final CustomerUserRepository customerUserRepository;
    private final EmployeeUserRepository employeeUserRepository;
    private final ManagerUserRepository managerUserRepository;

    @Autowired
    public UserMock(CustomerUserRepository customerUserRepository, EmployeeUserRepository employeeUserRepository, ManagerUserRepository managerUserRepository) {
        this.customerUserRepository = customerUserRepository;
        this.employeeUserRepository = employeeUserRepository;
        this.managerUserRepository = managerUserRepository;
    }

    public void mock() {
        UserFactory userFactory = new UserFactory();
        CustomerUser cu1 = (CustomerUser) userFactory.createUser("Alan", "Black", "alan@black.com", "none", UserType.CUSTOMER);
        CustomerUser cu2 = (CustomerUser) userFactory.createUser("Miriam", "Orange", "miriam@orange.com", "none", UserType.CUSTOMER);
        EmployeeUser eu1 = (EmployeeUser) userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE);
        ManagerUser mu1 = (ManagerUser) userFactory.createUser("Peter", "Tee", "peter@tee.com", "none", UserType.MANAGER);

        customerUserRepository.save(cu1);
        customerUserRepository.save(cu2);
        employeeUserRepository.save(eu1);
        managerUserRepository.save(mu1);
    }
}
