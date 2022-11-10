package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record TicketDTO(Long id, CustomerUserDTO owner, Set<EmployeeUserDTO> assignedEmployees,
        TicketStatus status, TicketPriority priority, Set<TicketMessageDTO> messages) {
    public static TicketDTO fromEntity(Ticket entity) {
        return new TicketDTO(
                entity.getTicketId(),
                CustomerUserDTO.fromEntity(entity.getOwner()),
                entity.getAssignedEmployees().stream().map(EmployeeUserDTO::fromEntity).collect(Collectors.toSet()),
                entity.getStatus(),
                entity.getPriority(),
                entity.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
