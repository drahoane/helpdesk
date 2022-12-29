package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketPriority;


public record CreateTicketDTO(String title, String message, TicketPriority priority) {
}
