package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;

public record EmployeeUserDTO(Long id, String firstName, String lastName, String email) {
    public static EmployeeUserDTO fromEntity(EmployeeUser entity) {
        return new EmployeeUserDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
}

