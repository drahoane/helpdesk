package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Notification;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
public class NotificationController {

    private  final NotificationService notificationService;


    @PostFilter("principal.username == filterObject.user.email")
    @GetMapping
    @Operation(description = "List of all owned notifications")
    public Set<Notification> getAllNotifications() {
        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    @PostAuthorize("principal.username == returnObject.user.email")
    @Operation(description = "Notification")
    public Notification getNotification(@NotNull @PathVariable Long id) {
        return notificationService.find(id);
    }
}
