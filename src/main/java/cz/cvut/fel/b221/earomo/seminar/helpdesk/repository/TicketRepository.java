package cz.cvut.fel.b221.earomo.seminar.helpdesk.repository;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Ticket;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public Set<Ticket> findAllByStatus(TicketStatus status);
}
