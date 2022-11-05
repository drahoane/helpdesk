package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long timeRecordId;

    @Column(name = "\"start\"")
    private LocalDateTime start;

    @Column(name = "\"end\"")
    private LocalDateTime end;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private EmployeeUser employee;
}
