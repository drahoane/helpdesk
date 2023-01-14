package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserUpdateDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User find(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Set<User> findAll() {
        return new HashSet<>(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Set<User> findAllByUserType(UserType userType) {
        return userRepository.findAll().stream().filter(x -> x.getUserType() == userType).collect(Collectors.toSet());
    }

    @Transactional
    public void update(String firstName, String lastName, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(User.class, "email", email));
        if(firstName != null) {
            user.setFirstName(firstName);
            log.info("User's first name has been updated to " + user.getFirstName());
        }

        if(lastName != null) {
            user.setLastName(lastName);
            log.info("User's last name has been updated to " + user.getLastName());
        }

        userRepository.save(user);
    }

    @Transactional
    public User create(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
                       @NotNull String password, @NotNull UserType userType, @NotNull Department department) {
        User user = userFactory.createUser(firstName, lastName, email, passwordEncoder.encode(password), userType, department);
        userRepository.save(user);

        log.info("Support member " + user.getUserId() + " with email " + user.getEmail() + " has been created");

        return user;
    }

    @Transactional
    public User createCustomer(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
                       @NotNull String password, @NotNull UserType userType) {
        User user = userFactory.createUser(firstName, lastName, email, passwordEncoder.encode(password), userType);
        userRepository.save(user);

        log.info("Customer " + user.getUserId() + " with email " + user.getEmail() + " has been created");

        return user;
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = userRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException(User.class, id);

        userRepository.deleteById(id);
        log.info("User " + id + " has been deleted");
    }
}
