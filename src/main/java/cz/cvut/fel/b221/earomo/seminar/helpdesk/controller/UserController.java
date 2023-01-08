package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientRequestException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security.EmailAlreadyTakenException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.CreateUserRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.UpdateUserRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Returns list of users, accepts optional parameter "type", that filters user based on their UserType.
     *
     * @param userType
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<UserDTO> getAllUsers(@RequestParam(required = false, name = "type") UserType userType) {
        if (userType == null)
            return userService.findAll().stream().map(UserDTO::fromEntity).collect(Collectors.toSet());

        return userService.findAllByUserType(userType).stream().map(UserDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.email()")
    public UserDTO getUser(@PathVariable @NotNull Long id) {
        return UserDTO.fromEntity(userService.find(id));
    }

    @PutMapping
    //@PreAuthorize("hasRole('ROLE_MANAGER') OR principal.username == request.email")
    public void updateUser(@RequestBody @Valid UpdateUserRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (request.getEmail().equals(auth.getName()) ||
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            userService.update(request.getFirstName(), request.getLastName(), request.getEmail());
        } else {
            throw new InsufficientPermissionsException(User.class, "update");
        }
    }

    @PostMapping
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest request) {
        if (userService.findAll().stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new EmailAlreadyTakenException(User.class, request.getEmail());
        }

        if (request.getUserType() == UserType.CUSTOMER)
            return UserDTO.fromEntity(userService.createCustomer(request.getFirstName(), request.getLastName(),
                    request.getEmail(), request.getPassword(), request.getUserType()));

        if (request.getDepartment() == null)
            throw new InsufficientRequestException(User.class, "create");

        return UserDTO.fromEntity(userService.create(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getUserType(), request.getDepartment()));
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


