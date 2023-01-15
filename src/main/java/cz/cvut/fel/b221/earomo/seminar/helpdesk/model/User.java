package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    @Getter(AccessLevel.PACKAGE)
    private String password;

    private boolean accountDisabled = false;

    @Transient
    private UserType userType;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Lazy
    private Set<TicketMessage> ticketMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Log> logs;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Notification> notifications;

    public abstract UserType getUserType();
}
