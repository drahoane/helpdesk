package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerUserRepository extends JpaRepository<ManagerUser, Long> {
}
