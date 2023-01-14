package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
