package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserUpdateDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;

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
        return userRepository.findAllByUserType(userType);
    }

    @Transactional
    public void update(@NotNull UserUpdateDTO userUpdateDTO) {
        User user = find(userUpdateDTO.id());
        if(userUpdateDTO.firstName() != null)
            user.setFirstName(userUpdateDTO.firstName());

        if(userUpdateDTO.lastName() != null) {
            user.setLastName(userUpdateDTO.lastName());
        }

        if(userUpdateDTO.email() != null) {
            user.setEmail(userUpdateDTO.email());
        }

        userRepository.save(user);
    }

    @Transactional
    public User create(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
                         @NotNull String password, @NotNull UserType userType) {
        User user = userFactory.createUser(firstName, lastName, email, password, userType);
        userRepository.save(user);

        return user;
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = userRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException(User.class, id);

        userRepository.deleteById(id);
    }
}
