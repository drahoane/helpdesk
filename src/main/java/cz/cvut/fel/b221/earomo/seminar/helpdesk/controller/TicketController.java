package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.CreateTicketDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketDetailDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.TicketUpdateDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.TicketService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
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

    /**
     * Returns all tickets of signed-in customer or all tickets if signed-in user is EMPLOYEE or MANAGER.
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
     * @param ticket
     */

    //TODO: Permissions
    @PutMapping
    public void updateTicket(@RequestBody TicketUpdateDTO ticket) {
        ticketService.update(ticket);
    }

    @PostMapping
    public TicketDetailDTO createTicket(Principal principal, @RequestBody @NotNull CreateTicketDTO ticket) {
        CustomerUser customer = (CustomerUser) userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "email", principal.getName()));

        return TicketDetailDTO.fromEntity(ticketService.create(customer, ticket.title(), ticket.message(), ticket.priority()));
    }

    @PutMapping
    @PostAuthorize("hasRole('ROLE_MANAGER') OR " )
    public void closeTicket(@RequestBody TicketUpdateDTO ticket) {
        ticketService.find(ticket.id()).setStatus(TicketStatus.RESOLVED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteTicket(@PathVariable @NotNull Long id) {
        ticketService.delete(id);
    }
}
