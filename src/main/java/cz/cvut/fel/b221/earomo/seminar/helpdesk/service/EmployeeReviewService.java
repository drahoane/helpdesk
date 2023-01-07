package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.dto.EmployeeReviewDTO;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeReviewService {
    private final EmployeeReviewRepository employeeReviewRepository;

    public boolean hasBeenReviewed(Ticket ticket) {
        return employeeReviewRepository.existsByTicket(ticket);
    }
    
    public Set<EmployeeReview> getAll() {
        return new HashSet<>(employeeReviewRepository.findAll());
    }

}
