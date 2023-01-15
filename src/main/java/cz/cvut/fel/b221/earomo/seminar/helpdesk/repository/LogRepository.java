package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LogRepository extends JpaRepository<Log, Long> {
    Set<Log> findAllByUser(User user);
}
