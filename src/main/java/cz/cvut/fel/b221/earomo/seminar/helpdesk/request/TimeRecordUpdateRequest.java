package cz.cvut.fel.b221.earomo.seminar.helpdesk.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties
public class TimeRecordUpdateRequest {
    @NotNull
    private Long timeRecordId;
    private LocalDateTime start;
    private LocalDateTime end;
}
