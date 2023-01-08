package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.EmployeeReviewDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.CustomerUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeReviewRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeReviewService {
    private final EmployeeReviewRepository employeeReviewRepository;
    private final TicketRepository ticketRepository;
    private final CustomerUserRepository customerUserRepository;

    public boolean hasBeenReviewed(Ticket ticket) {
        return employeeReviewRepository.existsByTicket(ticket);
    }

    @Transactional(readOnly = true)
    public Set<EmployeeReview> getAll() {
        return new HashSet<>(employeeReviewRepository.findAll());
    }

    @Transactional(readOnly = true)
    public EmployeeReview getById(@NotNull Long id) {
        return employeeReviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(EmployeeReview.class, id));
    }

    @Transactional(readOnly = true)
    public EmployeeReview getByTicket(@NotNull Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId));

        return employeeReviewRepository.getByTicket(ticket)
                .orElseThrow(() -> new ResourceNotFoundException(EmployeeReview.class, "ticket", ticket.getTicketId().toString()));
    }

    @Transactional
    public EmployeeReview create(@NotNull Long ticketId, @NotNull EmployeeReviewGrade grade, @NotNull String text, @NotNull CustomerUser writer) {
        EmployeeReview employeeReview = new EmployeeReview();
        employeeReview.setCreatedAt(LocalDateTime.now());
        employeeReview.setTicket(ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId)));
        employeeReview.setGrade(grade);
        employeeReview.setTextReview(text);
        employeeReview.setCustomer(writer);

        employeeReviewRepository.save(employeeReview);
        return employeeReview;
    }

    @Transactional
    public void delete(@NotNull Long id) {
        boolean exists = employeeReviewRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException(EmployeeReview.class, id);
        employeeReviewRepository.deleteById(id);
    }
}
