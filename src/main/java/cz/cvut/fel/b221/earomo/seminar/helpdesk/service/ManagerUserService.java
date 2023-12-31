package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ManagerUserService {
    private final ManagerUserRepository managerUserRepository;
    private final UserFactory userFactory;

    @Transactional
    public ManagerUser create(String firstName, String lastName, String email, String password) {
        ManagerUser managerUser = (ManagerUser) userFactory.createUser(firstName, lastName, email, password, UserType.MANAGER);
        managerUserRepository.save(managerUser);

        log.info("Manager " + managerUser.getUserId() + " with email " + managerUser.getEmail() + " has been created");

        return managerUser;
    }

    @Transactional(readOnly = true)
    public Optional<ManagerUser> find(@NotNull Long id) {
        return managerUserRepository.findById(id);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = managerUserRepository.existsById(id);
        if (!exists) throw new ResourceNotFoundException(Ticket.class, id);
        managerUserRepository.deleteById(id);
        log.info("Manager " + id + " has been deleted");
    }
}
