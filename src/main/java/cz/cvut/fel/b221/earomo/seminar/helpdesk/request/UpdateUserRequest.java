package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    @NotNull
    private String email;
}
