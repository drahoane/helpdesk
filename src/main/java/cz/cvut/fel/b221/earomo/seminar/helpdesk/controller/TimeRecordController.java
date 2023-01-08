package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TimeRecordDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.TimeRecordUpdateRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TimeRecordService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/time-record")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE')")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<TimeRecordDTO> getAllTimeRecords() {
        return timeRecordService.findAll().stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<TimeRecordDTO> getAllEmployeeTimeRecords(@PathVariable @NotNull Long employeeId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        Set<TimeRecord> timeRecords = timeRecordService.findAllByEmployee(employeeId).stream().filter(x -> securityUser.hasRole(Role.MANAGER) || x.getEmployee().getUserId() == securityUser.getUser().getUserId()).collect(Collectors.toSet());
        return timeRecords.stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @PostFilter("hasRole('ROLE_MANAGER') OR filterObject.employeeUser().email() == principal.username")
    @GetMapping("/ticket/{ticketId}")
    public Set<TimeRecordDTO> getAllTicketTimeRecords(@PathVariable @NotNull Long ticketId) {
        return timeRecordService.findByTicket(ticketId).stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{ticketId}/start")
    public TimeRecordDTO startTimeRecord(@PathVariable @NotNull Long ticketId) {
        return TimeRecordDTO.fromEntity(timeRecordService.create(ticketId));
    }

    @GetMapping("/stop")
    public TimeRecordDTO stopTimeRecord() {
        TimeRecord timeRecord = timeRecordService.getRunning(SecurityUtils.getCurrentUser().getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(TimeRecord.class, "end", "null"));

        timeRecordService.update(timeRecord.getTimeRecordId(), null, LocalDateTime.now());

        return TimeRecordDTO.fromEntity(timeRecord);
    }

    @PutMapping
    public void updateTimeRecord(@RequestBody @Valid TimeRecordUpdateRequest request) {
        timeRecordService.update(request.getTimeRecordId(), request.getStart(), request.getEnd());
    }
}
