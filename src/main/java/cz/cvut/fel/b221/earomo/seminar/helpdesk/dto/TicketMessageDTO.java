package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;

import java.time.LocalDateTime;

public record TicketMessageDTO (Long id, UserDTO sender, String message, LocalDateTime createdAt) {
    public static TicketMessageDTO fromEntity(TicketMessage entity) {
        return new TicketMessageDTO(
                entity.getTicketMessageId(),
                UserDTO.fromEntity(entity.getSender()),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }
}
