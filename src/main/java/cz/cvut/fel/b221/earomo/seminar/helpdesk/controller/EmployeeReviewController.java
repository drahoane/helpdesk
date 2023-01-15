package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.EmployeeReviewListDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.EmployeeReviewRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeReviewService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.LogService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee-review")
@AllArgsConstructor
public class EmployeeReviewController {
    private final EmployeeReviewService employeeReviewService;
    private final LogService logService;


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    @Operation(description = "List of all reviews")
    public Set<EmployeeReviewListDTO> getAllReviews() {
        return employeeReviewService.getAll().stream().map(EmployeeReviewListDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Review detail")
    public EmployeeReviewListDTO getReview(@PathVariable @NotNull Long ticketId) {
        return EmployeeReviewListDTO.fromEntity(employeeReviewService.getByTicket(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(description = "Create review")
    public EmployeeReviewListDTO createReview(@RequestBody @Valid EmployeeReviewRequest request) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        CustomerUser customer = (CustomerUser) securityUser.getUser();

        logService.createLogByTemplate(LogType.CREATE, securityUser.getUser(), EmployeeReview.class, SecurityUtils.getCurrentUserIp());
        return EmployeeReviewListDTO.fromEntity(employeeReviewService.create(request.getTicketId(), request.getGrade(), request.getText(), customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(description = "Delete review")
    public void deleteReview(@PathVariable @NotNull Long id) {
        employeeReviewService.delete(id);
        logService.createLogByTemplate(LogType.DELETE, SecurityUtils.getCurrentUser().getUser(), EmployeeReview.class, SecurityUtils.getCurrentUserIp());
    }
}
