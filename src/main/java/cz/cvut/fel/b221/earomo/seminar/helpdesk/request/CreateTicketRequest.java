package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Department;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateTicketRequest {
    @NotNull
    private String title;
    @NotNull
    private String message;
    @NotNull
    private TicketPriority priority;
    @NotNull
    private Department department;
}
