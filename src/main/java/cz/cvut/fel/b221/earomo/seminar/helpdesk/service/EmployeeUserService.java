package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmployeeUserService {
    private final EmployeeUserRepository employeeUserRepository;

    @Autowired
    public EmployeeUserService(EmployeeUserRepository employeeUserRepository) {
        this.employeeUserRepository = employeeUserRepository;
    }

    public EmployeeUser create(String firstName, String lastName, String email, String password) {
        UserFactory userFactory = new UserFactory();

        EmployeeUser employeeUser = (EmployeeUser)userFactory.createUser(firstName, lastName, email, password, UserType.EMPLOYEE);
        employeeUserRepository.save(employeeUser);

        return employeeUser;
    }

    public Set<EmployeeUser> getAllUnassignedEmployees() {
        return employeeUserRepository.getAllUnassignedEmployees();
    }
}
