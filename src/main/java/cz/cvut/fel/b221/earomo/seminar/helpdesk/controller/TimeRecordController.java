package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TimeRecordDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.TimeRecordUpdateRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TimeRecordService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private final LogService logService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "List of all time records")
    public Set<TimeRecordDTO> getAllTimeRecords() {
        return timeRecordService.findAll().stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "List of all time records of specified employee")
    public Set<TimeRecordDTO> getAllEmployeeTimeRecords(@PathVariable @NotNull Long employeeId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        Set<TimeRecord> timeRecords = timeRecordService.findAllByEmployee(employeeId).stream().filter(x -> securityUser.hasRole(Role.MANAGER) || x.getEmployee().getUserId() == securityUser.getUser().getUserId()).collect(Collectors.toSet());
        return timeRecords.stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @PostFilter("hasRole('ROLE_MANAGER') OR filterObject.employeeUser().email() == principal.username")
    @GetMapping("/ticket/{ticketId}")
    @Operation(description = "List of all time records linked to specified ticket")
    public Set<TimeRecordDTO> getAllTicketTimeRecords(@PathVariable @NotNull Long ticketId) {
        return timeRecordService.findByTicket(ticketId).stream().map(TimeRecordDTO::fromEntity).collect(Collectors.toSet());
    }

    @PostMapping("/{ticketId}")
    @Operation(description = "Start new time record")
    public TimeRecordDTO startTimeRecord(@PathVariable @NotNull Long ticketId) {
        logService.createLogByTemplate(LogType.UPDATE, SecurityUtils.getCurrentUser().getUser(), TimeRecord.class, SecurityUtils.getCurrentUserIp());
        return TimeRecordDTO.fromEntity(timeRecordService.create(ticketId));
    }

    @PatchMapping
    @Operation(description = "Stop running time record")
    public TimeRecordDTO stopTimeRecord() {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        TimeRecord timeRecord = timeRecordService.getRunning(securityUser.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(TimeRecord.class, "end", "null"));

        timeRecordService.update(timeRecord.getTimeRecordId(), null, LocalDateTime.now());
        logService.createLogByTemplate(LogType.UPDATE, securityUser.getUser(), TimeRecord.class, SecurityUtils.getCurrentUserIp());


        return TimeRecordDTO.fromEntity(timeRecord);
    }

    @PutMapping
    @Operation(description = "Update time record")
    public void updateTimeRecord(@RequestBody @Valid TimeRecordUpdateRequest request) {
        timeRecordService.update(request.getTimeRecordId(), request.getStart(), request.getEnd());
        logService.createLogByTemplate(LogType.UPDATE, SecurityUtils.getCurrentUser().getUser(), TimeRecord.class, SecurityUtils.getCurrentUserIp());
    }
}
