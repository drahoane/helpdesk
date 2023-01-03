package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security.EmailAlreadyTakenException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.*;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final CustomerUserService customerUserService;
    private final EmployeeUserService employeeUserService;
    private final ManagerUserService managerUserService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    /**
     * Returns list of users, accepts optional parameter "type", that filters user based on their UserType.
     * @param userType
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<User> getAllUsers(@RequestParam(required = false, name = "type") UserType userType) {
        if(userType == null)
            return userService.findAll();

        return userService.findAllByUserType(userType);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.email()")
    public UserDTO getUser(@PathVariable @NotNull Long id) {
        return UserDTO.fromEntity(userService.find(id));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') OR principal.username == userDTO.email()")
    public void updateUser(@RequestBody @NotNull UserUpdateDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(userDTO.email().equals(auth.getName()) ||
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            userService.update(userDTO);
        } else {
            throw new InsufficientPermissionsException(User.class, userDTO.id(), "update");
        }
    }

    @PostMapping
    //@PreAuthorize("hasRole('ROLE_MANAGER')")
    public UserDTO createUser(@RequestBody @NotNull CreateUserDTO createUserDTO) {
        if(userService.findAll().stream().anyMatch(u -> u.getEmail().equals(createUserDTO.email()))) {
            throw new EmailAlreadyTakenException(User.class, createUserDTO.email());
        }
        return UserDTO.fromEntity(userService.create(createUserDTO.firstName(), createUserDTO.lastName(),
                createUserDTO.email(), createUserDTO.password(), createUserDTO.userType()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteUser(@PathVariable @NotNull Long id) {
        userService.delete(id);
    }





    /*
    public Set<EmployeeReviewListDTO> getAllReviews(@PathVariable @NotNull Long id) {
        return employeeUserService.getAllReviews().stream().map(EmployeeReviewListDTO::fromEntity).collect(Collectors.toSet());
    }*/
}


