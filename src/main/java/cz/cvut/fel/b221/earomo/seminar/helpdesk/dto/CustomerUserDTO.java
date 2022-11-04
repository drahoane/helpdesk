package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;

public record CustomerUserDTO(Long customerId, String firstName, String lastName, String email) {
    public static CustomerUserDTO fromEntity(CustomerUser entity) {
        return new CustomerUserDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
}
