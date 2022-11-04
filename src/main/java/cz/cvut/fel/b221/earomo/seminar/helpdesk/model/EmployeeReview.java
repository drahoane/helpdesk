package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

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

    @OneToOne
    private EmployeeUser employee;

    @OneToOne
    private CustomerUser customer;

    @OneToOne
    private Ticket ticket;

    private Integer grade;
    
    private String textReview;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setGrade(int grade) {
        if(grade < 0 || grade > 5)
            throw new IllegalArgumentException("Grade can only be in rage 0-5.");

        this.grade = grade;
    }
}
