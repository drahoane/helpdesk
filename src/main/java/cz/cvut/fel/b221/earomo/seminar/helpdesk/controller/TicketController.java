package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketDetailDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketMessageDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.AssignEmployeeRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.CreateTicketRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.TicketUpdateRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/ticket")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final EmployeeUserService employeeUserService;
    private final LogService logService;

    /**
     * Returns all tickets of signed-in customer or all tickets if signed-in user is EMPLOYEE or MANAGER.
     *
     * @return
     */
    @PostFilter("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE') OR principal.username == filterObject.owner().email()")
    @GetMapping
    @Operation(description = "List of all tickets (MANAGER, EMPLOYEE) | List of all owned tickets (CUSTOMER)")
    public Set<TicketDetailDTO> getAllTickets() {
        Set<Ticket> tickets = ticketService.findAll();

        if (SecurityUtils.getCurrentUser().isEmployee())
            tickets = tickets.stream().filter(
                    t -> t.getAssignedEmployees().stream().anyMatch(
                            e -> e.getUserId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())
                    )
            ).collect(Collectors.toSet());

        return tickets.stream().map(TicketDetailDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.owner().email()")
    @Operation(description = "Ticket detail")
    public TicketDetailDTO getTicket(@PathVariable @NotNull Long id) {
        Ticket ticket = ticketService.find(id);
        if (SecurityUtils.getCurrentUser().isEmployee() && !SecurityUtils.getCurrentUser().isAssignedToTicket(ticket)) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, SecurityUtils.getCurrentUser().getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(Ticket.class, ticket.getTicketId(), "read");
        }

        return TicketDetailDTO.fromEntity(ticketService.find(id));
    }

    /**
     * Updates status and/or priority
     *
     * @param request
     */

    @PutMapping
    @Operation(description = "Ticket update")
    public void updateTicket(@RequestBody @Valid TicketUpdateRequest request) {
        Ticket ticket = ticketService.find(request.getTicketId());
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (securityUser.isCustomer() && !securityUser.ownsTicket(ticket) ||
                securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket)
        ) {
            // Ticket can be updated only by ticket owner, assigned employee and manager
            logService.createLogByTemplate(LogType.UNAUTHORIZED, securityUser.getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(Ticket.class, request.getTicketId(), "update");
        }

        if (securityUser.isCustomer() && request.getPriority() != null) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, securityUser.getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(Ticket.class, request.getTicketId(), "update priority");
        }

        ticketService.update(request.getTicketId(), request.getPriority(), request.getStatus());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(description = "Create ticket")
    public TicketDetailDTO createTicket(@RequestBody @Valid CreateTicketRequest request) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        CustomerUser customer = (CustomerUser) securityUser.getUser();

        Ticket ticket = ticketService.create(customer, request.getTitle(), request.getMessage(), request.getPriority(), request.getDepartment());

        logService.createLogByTemplate(LogType.CREATE, securityUser.getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());

        return TicketDetailDTO.fromEntity(ticket);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Close ticket")
    public void closeTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.find(id);
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (securityUser.isCustomer() && !securityUser.ownsTicket(ticket) ||
                securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket)
        ) {
            // Ticket can be closed only by ticket owner, assigned employee and manager
            logService.createLogByTemplate(LogType.UNAUTHORIZED, securityUser.getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(Ticket.class, id, "close");
        } else {
            ticketService.update(ticket.getTicketId(), null, TicketStatus.RESOLVED);
            logService.createLogByTemplate(LogType.UPDATE, securityUser.getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Delete ticket")
    public void deleteTicket(@PathVariable @NotNull Long id) {
        ticketService.delete(id);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Assign employee")
    public void assignEmployee(@PathVariable @NotNull Long id, @RequestBody @Valid AssignEmployeeRequest request) {
        ticketService.assignEmployee(ticketService.find(id), employeeUserService.find(request.getEmployeeId()));
    }

    @PostMapping("/{id}/unassign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Unassign employee")
    public void unassignEmployee(@PathVariable @NotNull Long id, @RequestBody @Valid AssignEmployeeRequest request) {
        ticketService.unassignEmployee(id, request.getEmployeeId());
    }

    @PostFilter("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE') OR principal.username == filterObject.sender().email()")
    @GetMapping("/{id}/message")
    @Operation(description = "List of messages linked to a specified ticket")
    public Set<TicketMessageDTO> getTicketMessages(@PathVariable @NotNull Long id) {
        Ticket ticket = ticketService.find(id);
        if (SecurityUtils.getCurrentUser().getUser().getUserType().equals(UserType.EMPLOYEE) && ticket.getAssignedEmployees().stream().noneMatch(e -> e.getUserId().equals(SecurityUtils.getCurrentUser().getUser().getUserId()))) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, SecurityUtils.getCurrentUser().getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(TicketMessage.class, id, "get");
        }
        return ticket.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet());
    }

    @PostMapping("/{id}/message")
    @Operation(description = "Send ticket message")
    public TicketMessageDTO addTicketMessage(@PathVariable @NotNull Long id,
                                             @RequestBody @NotNull String message) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        Ticket ticket = ticketService.find(id);
        if (!securityUser.ownsTicket(ticket) && !securityUser.isManager() && !securityUser.isAssignedToTicket(ticket)) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, SecurityUtils.getCurrentUser().getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(TicketMessage.class, id, "create");
        }

        if(ticket.getStatus().equals(TicketStatus.RESOLVED)) {
            logService.createLogByTemplate(LogType.UNAUTHORIZED, SecurityUtils.getCurrentUser().getUser(), Ticket.class, SecurityUtils.getCurrentUserIp());
            throw new InsufficientPermissionsException(TicketMessage.class, id, "create");
        }

        return TicketMessageDTO.fromEntity(ticketService.addTicketMessage(SecurityUtils.getCurrentUser().getUser(), id, message));
    }
}
