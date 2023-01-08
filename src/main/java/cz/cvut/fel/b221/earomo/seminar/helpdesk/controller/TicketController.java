package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketDetailDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketMessageDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.TicketStatus;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.AssignEmployeeRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.CreateTicketRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.TicketUpdateRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
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
    private final UserService userService;
    private final EmployeeUserService employeeUserService;

    /**
     * Returns all tickets of signed-in customer or all tickets if signed-in user is EMPLOYEE or MANAGER.
     *
     * @return
     */
    @PostFilter("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE') OR principal.username == filterObject.owner().email()")
    @GetMapping
    public Set<TicketDetailDTO> getAllTickets() {
        Set<Ticket> tickets = ticketService.findAll();

        if(SecurityUtils.getCurrentUser().isEmployee())
            tickets = tickets.stream().filter(
                    t -> t.getAssignedEmployees().stream().anyMatch(
                            e -> e.getUserId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())
                    )
            ).collect(Collectors.toSet());

        return tickets.stream().map(TicketDetailDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.owner().email()")
    public TicketDetailDTO getTicket(@PathVariable @NotNull Long id) {
        Ticket ticket = ticketService.find(id);
        if(SecurityUtils.getCurrentUser().isEmployee() && !SecurityUtils.getCurrentUser().isAssignedToTicket(ticket))
            throw new InsufficientPermissionsException(Ticket.class, ticket.getTicketId(), "read");

        return TicketDetailDTO.fromEntity(ticketService.find(id));
    }

    /**
     * Updates status and/or priority
     *
     * @param request
     */

    @PutMapping
    public void updateTicket(@RequestBody @Valid TicketUpdateRequest request) {
        Ticket ticket = ticketService.find(request.getTicketId());
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (securityUser.isCustomer() && !securityUser.ownsTicket(ticket) ||
                securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket)
        ) {
            // Ticket can be updated only by ticket owner, assigned employee and manager
            throw new InsufficientPermissionsException(Ticket.class, request.getTicketId(), "update");
        }
        
        if(securityUser.isCustomer() && request.getPriority() != null) {
            throw new InsufficientPermissionsException(Ticket.class, request.getTicketId(), "update priority");
        }

        ticketService.update(request.getTicketId(), request.getPriority(), request.getStatus());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public TicketDetailDTO createTicket(@RequestBody @Valid CreateTicketRequest request) {
        CustomerUser customer = (CustomerUser) SecurityUtils.getCurrentUser().getUser();

        Ticket ticket = ticketService.create(customer, request.getTitle(), request.getMessage(), request.getPriority(), request.getDepartment());

        return TicketDetailDTO.fromEntity(ticket);
    }

    @GetMapping("/{id}/close")
    public void closeTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.find(id);
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (securityUser.isCustomer() && !securityUser.ownsTicket(ticket) ||
            securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket)
        ) {
            // Ticket can be closed only by ticket owner, assigned employee and manager
            throw new InsufficientPermissionsException(Ticket.class, id, "close");
        } else {
            ticketService.update(ticket.getTicketId(), null, TicketStatus.RESOLVED);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteTicket(@PathVariable @NotNull Long id) {
        ticketService.delete(id);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void assignEmployee(@PathVariable @NotNull Long id, @RequestBody @Valid AssignEmployeeRequest request) {
        ticketService.assignEmployee(ticketService.find(id), employeeUserService.find(request.getEmployeeId()));
    }

    @PostMapping("/{id}/unassign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void unassignEmployee(@PathVariable @NotNull Long id, @RequestBody @Valid AssignEmployeeRequest request) {
        ticketService.unassignEmployee(id, request.getEmployeeId());
    }

    @PostFilter("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE') OR principal.username == filterObject.sender().email()")
    @GetMapping("/{id}/message")
    public Set<TicketMessageDTO> getTicketMessages(@PathVariable @NotNull Long id) {
        Ticket ticket = ticketService.find(id);
        if(SecurityUtils.getCurrentUser().getUser().getUserType().equals(UserType.EMPLOYEE) && ticket.getAssignedEmployees().stream().noneMatch(e -> e.getUserId().equals(SecurityUtils.getCurrentUser().getUser().getUserId()))) {
            throw new InsufficientPermissionsException(TicketMessage.class, id, "get");
        }
        return ticket.getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{ticketId}/message/{msgId}") // This could be useful in some cases, but I don't think it'll be useful to us.
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.sender().email()")
    public TicketMessageDTO getTicketMessage(@PathVariable @NotNull Long ticketId, @PathVariable @NotNull Long msgId) {
        Ticket ticket = ticketService.find(ticketId);
        if(SecurityUtils.getCurrentUser().getUser().getUserType().equals(UserType.EMPLOYEE) && ticket.getAssignedEmployees().stream().noneMatch(e -> e.getUserId().equals(SecurityUtils.getCurrentUser().getUser().getUserId()))) {
            throw new InsufficientPermissionsException(TicketMessage.class, msgId, "get");
        }
        return TicketMessageDTO.fromEntity(
                ticket.getMessages().stream().filter(m -> m.getTicketMessageId().equals(msgId)).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(TicketMessage.class, msgId)));
    }

    @PostMapping("/{id}/message/add")
    public TicketMessageDTO addTicketMessage(@PathVariable @NotNull Long id,
                                             @RequestBody @NotNull String message) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        Ticket ticket = ticketService.find(id);
        if (!securityUser.ownsTicket(ticket) && !securityUser.isManager() && !securityUser.isAssignedToTicket(ticket)) {
            throw new InsufficientPermissionsException(TicketMessage.class, id, "create");
        }
        return TicketMessageDTO.fromEntity(ticketService.addTicketMessage(SecurityUtils.getCurrentUser().getUser(), id, message));
    }
}
