package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EmployeeReviewRequest {
    @NotNull
    private Long ticketId;
    @NotNull
    private EmployeeReviewGrade grade;
    @NotNull
    private String text;
}
