package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.CreateTicketDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketDetailDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketMessageDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketUpdateDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeUserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        return ticketService.findAll().stream().map(TicketDetailDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') OR principal.username == returnObject.owner().email()")
    public TicketDetailDTO getTicket(@PathVariable @NotNull Long id) {
        return TicketDetailDTO.fromEntity(ticketService.find(id));
    }

    /**
     * Updates status and/or priority
     *
     * @param ticketDto
     */

    @PutMapping
    public void updateTicket(@RequestBody TicketUpdateDTO ticketDto) {
        Ticket ticket = ticketService.find(ticketDto.id());
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (ticket.getOwner().getUserId() != securityUser.getUser().getUserId() && !securityUser.hasAnyRole(Role.EMPLOYEE, Role.CUSTOMER)) {
            throw new InsufficientPermissionsException(Ticket.class, ticketDto.id(), "update");
        }

        // TODO

        ticketService.update(ticketDto);
    }

    @PostMapping
    public TicketDetailDTO createTicket(Principal principal, @RequestBody @NotNull CreateTicketDTO ticket) {
        CustomerUser customer = (CustomerUser) userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "email", principal.getName()));

        return TicketDetailDTO.fromEntity(ticketService.create(customer, ticket.title(), ticket.message(), ticket.priority()));
    }

    @PutMapping("/{id}/close")
    public void closeTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.find(id);
        SecurityUser securityUser = SecurityUtils.getCurrentUser();

        if (ticket.getOwner().getUserId() != securityUser.getUser().getUserId() &&
                securityUser.hasRole(Role.MANAGER) &&
                !(securityUser.getUser().getUserType() == UserType.EMPLOYEE &&
                        ((EmployeeUser)securityUser.getUser()).getAssignedTickets().contains(ticket))
        ) {
            // Ticket can be closed only by ticket owner, assigned employee and manager
            throw new InsufficientPermissionsException(Ticket.class, id, "close");
        } else {
            ticket.setStatus(TicketStatus.RESOLVED);
            ticketService.update(TicketUpdateDTO.fromEntity(ticket));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteTicket(@PathVariable @NotNull Long id) {
        ticketService.delete(id);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void assignEmployee(@PathVariable @NotNull Long id, @RequestBody Long employeeId) {
        ticketService.assignEmployee(ticketService.find(id), employeeUserService.find(employeeId));
    }

    // TODO: Permissions
    @GetMapping("/{id}/message")
    public Set<TicketMessageDTO> getTicketMessages(@PathVariable @NotNull Long id) {
        return ticketService.find(id).getMessages().stream().map(TicketMessageDTO::fromEntity).collect(Collectors.toSet());
    }

    // TODO: Permissions
    @GetMapping("/{ticketID}/message/{msgID}") // This could be useful in some cases, but I don't think it'll be useful to us.
    public TicketMessageDTO getTicketMessage(@PathVariable @NotNull Long ticketID, @PathVariable @NotNull Long msgID) {
        return TicketMessageDTO.fromEntity(
                ticketService.find(ticketID)
                        .getMessages().stream().filter(m -> m.getTicketMessageId().equals(msgID)).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(TicketMessage.class, msgID)));
    }

    // TODO: Permissions
    @PostMapping("/{id}/message/add")
    public TicketMessageDTO addTicketMessage(Principal principal, @PathVariable @NotNull Long id,
                                             @RequestBody @NotNull String message) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "email", principal.getName()));
        return TicketMessageDTO.fromEntity(ticketService.addTicketMessage(user, id, message));
    }
}
