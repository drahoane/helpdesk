package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record TicketUpdateDTO(Long id, TicketStatus status, TicketPriority priority, Set<UserDTO> assignedEmployees) {
    public static TicketUpdateDTO fromEntity(Ticket entity) {
        return new TicketUpdateDTO(
                entity.getTicketId(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getAssignedEmployees().stream().map(UserDTO::fromEntity).collect(Collectors.toSet()));
    }
}
