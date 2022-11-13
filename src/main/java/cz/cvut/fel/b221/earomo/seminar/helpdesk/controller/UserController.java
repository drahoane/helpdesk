package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.CustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final CustomerUserService customerUserService;

    @Autowired
    public UserController(CustomerUserService customerUserService) {
        this.customerUserService = customerUserService;
    }


}


