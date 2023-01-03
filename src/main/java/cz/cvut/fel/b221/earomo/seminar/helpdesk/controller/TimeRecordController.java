package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TimeRecordService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/timeRecord")
@AllArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<TimeRecord> getAllTimeRecords() {
        return timeRecordService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{ticketId}")
    public Set<TimeRecord> getTimeRecord(@PathVariable @NotNull Long ticketId) {
        return timeRecordService.findByTicket(ticketId);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Set<TimeRecord> getAllEmployeeTimeRecords(@PathVariable @NotNull Long employeeId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        return timeRecordService.findAllByEmployee(employeeId).stream().filter(x -> securityUser.hasRole(Role.MANAGER) || x.getEmployee().getUserId() == securityUser.getUser().getUserId()).collect(Collectors.toSet());
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{ticketId}/start")
    public TimeRecord startTimeRecord(@PathVariable @NotNull Long ticketId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        return timeRecordService.create(ticketId, securityUser.getUser().getUserId());
    }

    @GetMapping("/{timeRecordId}/stop")
    public void stopTimeRecord(@PathVariable @NotNull Long timeRecordId) {
        TimeRecord timeRecord = timeRecordService.findById(timeRecordId);
        if(timeRecord.getEnd() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Time record #" + timeRecordId + " has already been stopped.");
        }
        timeRecord.setEnd(LocalDateTime.now());
    }

}
