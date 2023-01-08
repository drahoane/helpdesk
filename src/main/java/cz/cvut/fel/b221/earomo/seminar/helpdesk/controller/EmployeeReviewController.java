package cz.cvut.fel.b221.earomo.seminar.helpdesk.controller;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.EmployeeReviewListDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.request.EmployeeReviewRequest;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.service.EmployeeReviewService;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
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


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public Set<EmployeeReviewListDTO> getAllReviews() {
        return employeeReviewService.getAll().stream().map(EmployeeReviewListDTO::fromEntity).collect(Collectors.toSet());
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public EmployeeReviewListDTO getReview(@PathVariable @NotNull Long ticketId) {
        return EmployeeReviewListDTO.fromEntity(employeeReviewService.getByTicket(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public EmployeeReviewListDTO createReview(@RequestBody @Valid EmployeeReviewRequest request) {
        CustomerUser customer = (CustomerUser) SecurityUtils.getCurrentUser().getUser();

        return EmployeeReviewListDTO.fromEntity(employeeReviewService.create(request.getTicketId(), request.getGrade(), request.getText(), customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteReview(@PathVariable @NotNull Long id) {
        employeeReviewService.delete(id);
    }
}
