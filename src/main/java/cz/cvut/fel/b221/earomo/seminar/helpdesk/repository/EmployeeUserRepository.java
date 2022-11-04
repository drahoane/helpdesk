package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeUserRepository extends JpaRepository<EmployeeUser, Long> {
}
