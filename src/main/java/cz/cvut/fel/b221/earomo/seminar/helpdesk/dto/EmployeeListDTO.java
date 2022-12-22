package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeListDTO(Long id, String firstName, String lastName, String email, Set<TicketDetailDTO> assignedTickets) {
    public static EmployeeListDTO fromEntity(EmployeeUser entity) {
        return new EmployeeListDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getAssignedTickets().stream().map(TicketDetailDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
