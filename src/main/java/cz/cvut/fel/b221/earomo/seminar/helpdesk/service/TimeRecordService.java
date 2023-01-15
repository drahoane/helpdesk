package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.InsufficientPermissionsException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.*;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TicketRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.TimeRecordRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final TicketRepository ticketRepository;
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
    public TimeRecord findById(@NotNull Long id) {
        return timeRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TimeRecord.class, id));
    }

    @Transactional(readOnly = true)
    public Set<TimeRecord> findByTicket(@NotNull Long ticketId) {
        return timeRecordRepository.findAllByTicket(ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId)));
    }

    @Transactional
    public TimeRecord create(Long ticketId) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;
        assert !securityUser.isCustomer();

        if (timeRecordRepository.existsByEndIsNullAndEmployee((EmployeeUser) securityUser.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "One TimeRecord is already running.");
        }

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException(Ticket.class, ticketId));

        if (securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket))
            throw new InsufficientPermissionsException(TimeRecord.class, "create");

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setStart(LocalDateTime.now());
        timeRecord.setTicket(ticket);
        timeRecord.setEmployee((EmployeeUser) securityUser.getUser());

        timeRecordRepository.save(timeRecord);
        log.info("Time record " + timeRecord.getTimeRecordId() + " has been created");

        return timeRecord;
    }

    @Transactional
    public void update(@NotNull Long id, LocalDateTime start, LocalDateTime end) {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        assert securityUser != null;
        assert !securityUser.isCustomer();

        TimeRecord timeRecord = timeRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TimeRecord.class, id));
        Ticket ticket = timeRecord.getTicket();

        if (securityUser.isEmployee() && !securityUser.isAssignedToTicket(ticket))
            throw new InsufficientPermissionsException(TimeRecord.class, "update");

        if(start != null) {
            timeRecord.setStart(start);
            log.info("Time record's start has been updated to " + timeRecord.getStart());
        }

        if(end != null) {
            timeRecord.setEnd(end);
            log.info("Time record's end has been updated to " + timeRecord.getEnd());
        }

        timeRecordRepository.save(timeRecord);
    }

    @Transactional(readOnly = true)
    public Optional<TimeRecord> getRunning(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
        assert !user.getUserType().equals(UserType.CUSTOMER);

        return timeRecordRepository.findFirstByEndIsNullAndEmployee((EmployeeUser) user);
    }
}
