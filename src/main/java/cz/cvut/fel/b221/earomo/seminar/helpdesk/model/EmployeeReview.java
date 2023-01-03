package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EmployeeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long employeeReviewId;

    @ManyToOne
    private CustomerUser customer;

    @OneToOne
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private EmployeeReviewGrade grade;
    
    private String textReview;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
