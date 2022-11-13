package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
