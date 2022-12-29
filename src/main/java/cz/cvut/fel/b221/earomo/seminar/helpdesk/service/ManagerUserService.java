package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.ManagerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
public class ManagerUserService {
    private final ManagerUserRepository managerUserRepository;
    private final UserFactory userFactory;
    private final TicketService ticketService;
    private final EmployeeUserService employeeUserService;

    public ManagerUser create(String firstName, String lastName, String email, String password) {
        ManagerUser managerUser = (ManagerUser)userFactory.createUser(firstName, lastName, email, password, UserType.MANAGER);
        managerUserRepository.save(managerUser);

        return managerUser;
    }

    @Transactional(readOnly = true)
    public Optional<ManagerUser> find(@NotNull Long id) {
        return managerUserRepository.findById(id);
    }

    // TODO: change password

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = managerUserRepository.existsById(id);
        if (!exists) throw new ResourceNotFoundException(Ticket.class, id);
        managerUserRepository.deleteById(id);
    }
}
