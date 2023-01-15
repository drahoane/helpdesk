package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/log")
@AllArgsConstructor
public class LogController {

    private  final LogService logService;


    @GetMapping
    @Operation(description = "List of all logs owned by logged in user")
    public Set<Log> getAllLogs() {
        return logService.findAllByUser(SecurityUtils.getCurrentUser().getUser().getUserId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "List of all logs owned by specified user")
    public Set<Log> getAllUserLogs(@NotNull @PathVariable Long userId) {
        return logService.findAllByUser(userId);
    }
}
