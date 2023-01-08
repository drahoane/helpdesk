package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    public Set<TimeRecord> findAllByTicket(Ticket ticket);
    public Optional<TimeRecord> findFirstByEndIsNullAndEmployee(EmployeeUser employeeUser);
    public boolean existsByEndIsNullAndEmployee(EmployeeUser employeeUser);
}
