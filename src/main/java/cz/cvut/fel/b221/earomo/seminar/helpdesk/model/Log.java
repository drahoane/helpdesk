package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Log {
    @GeneratedValue
    @Id
    private Long logId;
    private String resource;
    @ManyToOne
    private User user;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogType action;
    private String description;
    private String ip;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setResource(Class resource) {
        this.resource = resource.getSimpleName();
    }
    public void setResource(String resource) {
        this.resource = resource;
    }

    public Log copy() {
        Log log = new Log();
        log.setResource(this.getResource());
        log.setUser(this.getUser());
        log.setAction(this.getAction());
        log.setDescription(this.getDescription());
        log.setIp(this.getIp());
        log.setCreatedAt(this.getCreatedAt());

        return log;
    }
}
