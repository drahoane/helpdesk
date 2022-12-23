package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<ManagerUser> find(@NotNull Long id) {
        return managerUserRepository.findById(id);
    }

    @Transactional
    public void updateName(@NotNull Long id, String firstName, String lastName) {
        ManagerUser managerUser = find(id).orElseThrow(IllegalArgumentException::new);
        managerUser.setFirstName(firstName);
        managerUser.setLastName(lastName);

        managerUserRepository.save(managerUser);
    }

    @Transactional
    public void updateEmail(@NotNull Long id, String email) {
        ManagerUser managerUser = find(id).orElseThrow(IllegalArgumentException::new);
        managerUser.setEmail(email);

        managerUserRepository.save(managerUser);
    }

    // TODO: change password

    @Transactional
    public void delete(@NotNull Long id) {
        ManagerUser managerUser = find(id).orElseThrow(IllegalArgumentException::new);
        managerUserRepository.delete(managerUser);
        //persist
    }
}
