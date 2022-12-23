package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerUserService {
    private final CustomerUserRepository customerUserRepository;

    @Autowired
    public CustomerUserService(CustomerUserRepository customerUserRepository) {
        this.customerUserRepository = customerUserRepository;
    }

    public CustomerUser create(String firstName, String lastName, String email, String password) {
        UserFactory userFactory = new UserFactory();

        CustomerUser customerUser = (CustomerUser)userFactory.createUser(firstName, lastName, email, password, UserType.CUSTOMER);
        customerUserRepository.save(customerUser);

        return customerUser;
    }

    @Transactional(readOnly = true)
    public Set<CustomerUser> findAll() {
        return new HashSet<>(customerUserRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Optional<CustomerUser> find(@NotNull Long id) {
        return customerUserRepository.findById(id);
    }

    @Transactional
    public void updateName(@NotNull Long id, String firstName, String lastName) {
        CustomerUser customerUser = find(id).orElseThrow(IllegalArgumentException::new);
        customerUser.setFirstName(firstName);
        customerUser.setLastName(lastName);

        customerUserRepository.save(customerUser);
    }

    @Transactional
    public void updateEmail(@NotNull Long id, String email) {
        CustomerUser customerUser = find(id).orElseThrow(IllegalArgumentException::new);
        customerUser.setEmail(email);

        customerUserRepository.save(customerUser);
    }

    // TODO: change password

    @Transactional
    public void delete(@NotNull Long id) {
        CustomerUser customerUser = find(id).orElseThrow(IllegalArgumentException::new);
        customerUserRepository.delete(customerUser);
        //persist
    }
}
