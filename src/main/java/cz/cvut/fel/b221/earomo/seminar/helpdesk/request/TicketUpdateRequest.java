package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TicketUpdateRequest {
    @NotNull
    private Long ticketId;
    private TicketStatus status;
    private TicketPriority priority;
}
