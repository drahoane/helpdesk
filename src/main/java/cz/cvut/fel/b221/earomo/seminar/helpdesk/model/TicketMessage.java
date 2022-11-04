package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TicketMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long ticketMessageId;

    @ManyToOne
    private User sender;

    @ManyToOne
    private Ticket ticket;

    @Column(nullable = false)
    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
