package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TimeRecord;

import java.time.LocalDateTime;

public record TimeRecordDTO(Long timeRecordId, Long ticketId, LocalDateTime start, LocalDateTime end, UserDTO employeeUser) {
    public static TimeRecordDTO fromEntity(TimeRecord timeRecord) {
        return new TimeRecordDTO(timeRecord.getTimeRecordId(), timeRecord.getTicket().getTicketId(), timeRecord.getStart(), timeRecord.getEnd(), UserDTO.fromEntity(timeRecord.getEmployee()));
    }
}
