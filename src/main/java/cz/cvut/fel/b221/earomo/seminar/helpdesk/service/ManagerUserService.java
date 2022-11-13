package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerUserService {
    private final ManagerUserRepository managerUserRepository;

    @Autowired
    public ManagerUserService(ManagerUserRepository managerUserRepository) {
        this.managerUserRepository = managerUserRepository;
    }

    public ManagerUser create(String firstName, String lastName, String email, String password) {
        UserFactory userFactory = new UserFactory();

        ManagerUser managerUser = (ManagerUser)userFactory.createUser(firstName, lastName, email, password, UserType.MANAGER);
        managerUserRepository.save(managerUser);

        return managerUser;
    }
}
