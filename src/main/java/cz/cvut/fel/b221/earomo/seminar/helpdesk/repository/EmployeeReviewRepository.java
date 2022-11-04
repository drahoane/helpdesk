package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, Long> {
}
