package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, Long> {
    boolean existsByTicket(Ticket ticket);

    Optional<EmployeeReview> getByTicket(Ticket ticket);
}
