package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
}
