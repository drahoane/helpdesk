package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.EmployeeUserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TimeRecordRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final TicketRepository ticketRepository;
    private final EmployeeUserRepository employeeUserRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Set<TimeRecord> findAll() {
        return new HashSet<>(timeRecordRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Set<TimeRecord> findAllByEmployee(@NotNull Long id) {
        return timeRecordRepository.findAll().stream().filter(tr -> tr.getEmployee().getUserId().equals(id)).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<TimeRecord> findByTicket(@NotNull Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, id)).getTimeRecords();
    }

    @Transactional(readOnly = true)
    public TimeRecord findById(@NotNull Long id) {
        return timeRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TimeRecord.class, id));
    }

    @Transactional
    public TimeRecord create(Long ticketId, Long employeeId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;


        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId));
        EmployeeUser employee = employeeUserRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EmployeeUser.class, employeeId));

        if(securityUser.getUser().getUserType().equals(UserType.EMPLOYEE) && ticket.getAssignedEmployees().stream().noneMatch(x -> x.getUserId().equals(securityUser.getUser().getUserId())))
            throw new InsufficientPermissionsException(TimeRecord.class, "create");

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setStart(LocalDateTime.now());
        timeRecord.setTicket(ticket);
        timeRecord.setEmployee(employee);

        timeRecordRepository.save(timeRecord);
        ticketRepository.save(ticket);
        employeeUserRepository.save(employee);

        return timeRecord;
    }
}
