package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue
    private Long notificationId;
    @ManyToOne
    @JsonIgnore
    private User user;
    @Column(nullable = false)
    private String message;
}
