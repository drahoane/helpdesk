package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, Long> {
}
