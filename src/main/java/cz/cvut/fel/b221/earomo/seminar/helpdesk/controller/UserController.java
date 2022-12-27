package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.CustomerUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.ManagerUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final CustomerUserService customerUserService;
    private final EmployeeUserService employeeUserService;
    private final ManagerUserService managerUserService;
    private final UserService userService;




}


