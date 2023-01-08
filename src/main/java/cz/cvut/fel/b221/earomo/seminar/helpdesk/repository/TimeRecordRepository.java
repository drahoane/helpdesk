package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    Set<TimeRecord> findAllByTicket(Ticket ticket);

    Optional<TimeRecord> findFirstByEndIsNullAndEmployee(EmployeeUser employeeUser);

    boolean existsByEndIsNullAndEmployee(EmployeeUser employeeUser);
}
