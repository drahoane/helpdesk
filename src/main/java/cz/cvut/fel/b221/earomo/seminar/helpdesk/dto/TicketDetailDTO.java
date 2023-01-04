package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record TicketDetailDTO(Long id, UserDTO owner, Set<UserDTO> assignedEmployees, TicketStatus status,
                              TicketPriority priority, Department department, String title, Set<TicketMessageDTO> messages) {
    public static TicketDetailDTO fromEntity(Ticket entity) {
        return new TicketDetailDTO(
                entity.getTicketId(),
                UserDTO.fromEntity(entity.getOwner()),
                entity.getAssignedEmployees().stream().map(UserDTO::fromEntity).collect(Collectors.toSet()),
                entity.getStatus(),
                entity.getPriority(),
                entity.getDepartment(),
                entity.getTitle(),
                entity.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
