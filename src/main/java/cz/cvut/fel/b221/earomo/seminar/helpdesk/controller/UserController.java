package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.EmployeeTicketListDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketDetailDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.UserDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.CustomerUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.ManagerUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final CustomerUserService customerUserService;
    private final EmployeeUserService employeeUserService;
    private final ManagerUserService managerUserService;
    private final UserService userService;
    private final UserRepository userRepository;

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
}


