package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeUserService {
    private final EmployeeUserRepository employeeUserRepository;
    private final UserFactory userFactory;
    private final TicketRepository ticketRepository;

    public EmployeeUser create(String firstName, String lastName, String email, String password) {
        EmployeeUser employeeUser = (EmployeeUser)userFactory.createUser(firstName, lastName, email, password, UserType.EMPLOYEE);
        employeeUserRepository.save(employeeUser);

        return employeeUser;
    }

    @Transactional(readOnly = true)
    public Set<EmployeeUser> findAll() {
        return new HashSet<>(employeeUserRepository.findAll());
    }

    @Transactional(readOnly = true)
    public EmployeeUser find(@NotNull Long id) {
        return employeeUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(EmployeeUser.class, id));
    }

    @Transactional
    public void updateName(@NotNull Long id, String firstName, String lastName) {
        EmployeeUser employeeUser = find(id);
        employeeUser.setFirstName(firstName);
        employeeUser.setLastName(lastName);

        employeeUserRepository.save(employeeUser);
    }

    @Transactional
    public void updateEmail(@NotNull Long id, String email) {
        EmployeeUser employeeUser = find(id);
        employeeUser.setEmail(email);

        employeeUserRepository.save(employeeUser);
    }

    // TODO: change password

    @Transactional
    public void delete(@NotNull Long id) {
        EmployeeUser employeeUser = find(id);
        employeeUserRepository.delete(employeeUser);
    }

    public Set<EmployeeUser> getAllUnassignedEmployees() {
        Set<EmployeeUser> unassignedEmployees = new HashSet<>(employeeUserRepository.findAll());
        Set<EmployeeUser> assignedEmployees = new HashSet<>();
        Set<Ticket> unresolvedTickets = new HashSet<>();

        unresolvedTickets.addAll(ticketRepository.findAllByStatus(TicketStatus.OPEN));
        unresolvedTickets.addAll(ticketRepository.findAllByStatus(TicketStatus.AWAITING_RESPONSE));

        unresolvedTickets.stream().forEach(ticket -> assignedEmployees.addAll(ticket.getAssignedEmployees()));

        unassignedEmployees.removeAll(assignedEmployees);

        return unassignedEmployees;
    }
}
