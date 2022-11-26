package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public Set<EmployeeUser> findAll() {
        return new HashSet<>(employeeUserRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeUser> find(@NotNull Long id) {
        return employeeUserRepository.findById(id);
    }

    @Transactional
    public void updateName(@NotNull Long id, String firstName, String lastName) {
        EmployeeUser employeeUser = find(id).orElseThrow(IllegalArgumentException::new);
        employeeUser.setFirstName(firstName);
        employeeUser.setLastName(lastName);

        employeeUserRepository.save(employeeUser);
    }

    @Transactional
    public void updateEmail(@NotNull Long id, String email) {
        EmployeeUser employeeUser = find(id).orElseThrow(IllegalArgumentException::new);
        employeeUser.setEmail(email);

        employeeUserRepository.save(employeeUser);
    }

    // TODO: change password

    public Set<EmployeeUser> getAllUnassignedEmployees() {
        return employeeUserRepository.getAllUnassignedEmployees();
    }
}
