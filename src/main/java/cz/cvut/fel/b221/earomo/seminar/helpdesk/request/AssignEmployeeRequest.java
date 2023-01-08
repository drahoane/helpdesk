package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignEmployeeRequest {
    @NotNull
    private String employeeId;

    public Long getEmployeeId() {
        return Long.valueOf(employeeId);
    }
}
