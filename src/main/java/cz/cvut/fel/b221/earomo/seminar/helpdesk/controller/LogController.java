package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
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


    @PostFilter("hasRole('ROLE_MANAGER') OR principal.username == filterObject.user.email")
    @GetMapping
    @Operation(description = "List of all logs of certain type (MANAGER) | List of all owned logs of certain type (CUSTOMER, EMPLOYEE)")
    public Set<Log> getAllLogsByType(@NotNull @RequestBody LogType type) {
        return logService.findAllByType(type);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ROLE_MANAGER') OR principal.username == returnObject.user.email")
    @Operation(description = "Log")
    public Log getLog(@NotNull @PathVariable Long id) {
        return logService.find(id);
    }
}
