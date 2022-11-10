package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;

public record UserDTO(Long id, String firstName, String lastName, String email, UserType userType) {
    public static UserDTO fromEntity(User entity) {
        return new UserDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getUserType()
        );
    }
}
