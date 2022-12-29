package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;


public record UserUpdateDTO(Long id, String firstName, String lastName, String email) {
    public static UserUpdateDTO fromEntity(User entity) {
        return new UserUpdateDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail());
    }
}
