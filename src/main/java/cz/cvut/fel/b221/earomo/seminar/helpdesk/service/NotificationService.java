package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Notification;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.observer.Observer;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService implements Observer {
    private final NotificationRepository notificationRepository;

    @Override
    public void update(Object obj) {
        if(!(obj instanceof TicketMessage))
            throw new IllegalArgumentException();

        TicketMessage ticketMessage = (TicketMessage) obj;

        if(!ticketMessage.getSender().getUserType().equals(UserType.CUSTOMER)) {
            Notification notification = new Notification();
            notification.setUser(ticketMessage.getTicket().getOwner());
            notification.setMessage("You have new reply to ticket #" + ticketMessage.getTicket().getTicketId());

            notificationRepository.save(notification);
        }
    }
}
