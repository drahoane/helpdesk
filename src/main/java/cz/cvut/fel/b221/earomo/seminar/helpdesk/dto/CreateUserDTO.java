package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;

public record CreateUserDTO (String firstName, String lastName, String email, String password, UserType userType) {
}
