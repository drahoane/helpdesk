package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomerUserService {
    private final CustomerUserRepository customerUserRepository;
    private final UserFactory userFactory;


    public CustomerUser create(String firstName, String lastName, String email, String password) {
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

    // TODO: change password

    @Transactional
    public void delete(@NotNull Long id) {
        CustomerUser customerUser = find(id).orElseThrow(IllegalArgumentException::new);
        customerUserRepository.delete(customerUser);
        //persist
    }
}
