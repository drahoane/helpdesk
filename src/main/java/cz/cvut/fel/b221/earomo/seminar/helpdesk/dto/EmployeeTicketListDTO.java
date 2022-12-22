package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeTicketListDTO(Long id, Set<EmployeeUserDTO> assignedEmployees, TicketStatus status, TicketPriority priority,
                                    Set<TicketMessageDTO> messages, Set<TimeRecord> timeRecord) {
    public static EmployeeTicketListDTO fromEntity(Ticket entity) {
        return new EmployeeTicketListDTO(
                entity.getTicketId(),
                entity.getAssignedEmployees().stream().map(EmployeeUserDTO::fromEntity).collect(Collectors.toSet()),
                entity.getStatus(),
                entity.getPriority(),
                entity.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet()),
                entity.getTimeRecords()
        );
    }
}
