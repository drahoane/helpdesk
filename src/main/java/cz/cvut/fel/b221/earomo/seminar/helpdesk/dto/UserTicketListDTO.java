package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record UserTicketListDTO(Long id, TicketStatus status, Set<TicketMessageDTO> messages) {
    public static UserTicketListDTO fromEntity(Ticket entity) {
        return new UserTicketListDTO(
                entity.getTicketId(),
                entity.getStatus(),
                entity.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
