package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientRequestException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.security.EmailAlreadyTakenException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.CreateUserRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.UpdateUserRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
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
    private final LogService logService;

    /**
     * Returns list of users, accepts optional parameter "type", that filters user based on their UserType.
     *
     * @param userType
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "List of all users")
    public Set<UserDTO> getAllUsers(@RequestParam(required = false, name = "type") UserType userType) {
        if (userType == null)
            return userService.findAll().stream().map(UserDTO::fromEntity).collect(Collectors.toSet());

        return userService.findAllByUserType(userType).stream().map(UserDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.email()")
    @Operation(description = "User detail")
    public UserDTO getUser(@PathVariable @NotNull Long id) {
        return UserDTO.fromEntity(userService.find(id));
    }

    @PutMapping
    @Operation(description = "User update")
    public void updateUser(@RequestBody @Valid UpdateUserRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (request.getEmail().equals(auth.getName()) ||
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            userService.update(request.getFirstName(), request.getLastName(), request.getEmail());
            logService.createLogByTemplate(LogType.UPDATE, securityUser.getUser(), User.class, SecurityUtils.getCurrentUserIp());
        } else {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, securityUser.getUser(), User.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(User.class, "update");
        }
    }

    @PostMapping
    @Operation(description = "Create user")
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest request) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (userService.findAll().stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new EmailAlreadyTakenException(User.class, request.getEmail());
        }

        if (request.getUserType() == UserType.CUSTOMER) {
            logService.createLogByTemplate(LogType.CREATE, securityUser.getUser(), User.class, SecurityUtils.getCurrentUserIp());
            return UserDTO.fromEntity(userService.createCustomer(request.getFirstName(), request.getLastName(),
                    request.getEmail(), request.getPassword(), request.getUserType()));
        }

        if (request.getDepartment() == null) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, securityUser.getUser(), User.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientRequestException(User.class, "create");
        }

        logService.createLogByTemplate(LogType.CREATE, securityUser.getUser(), User.class, SecurityUtils.getCurrentUserIp());
        return UserDTO.fromEntity(userService.create(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getUserType(), request.getDepartment()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Delete user")
    public void deleteUser(@PathVariable @NotNull Long id) {
        logService.createLogByTemplate(LogType.DELETE, SecurityUtils.getCurrentUser().getUser(), User.class, SecurityUtils.getCurrentUserIp());
        userService.delete(id);
    }





    /*
    public Set<EmployeeReviewListDTO> getAllReviews(@PathVariable @NotNull Long id) {
        return employeeUserService.getAllReviews().stream().map(EmployeeReviewListDTO::fromEntity).collect(Collectors.toSet());
    }*/
}


