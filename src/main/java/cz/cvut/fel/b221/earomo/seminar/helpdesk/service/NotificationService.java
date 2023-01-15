package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Notification;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.observer.Observer;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.observer.TicketMessageNotifier;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.NotificationRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class NotificationService implements Observer {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TicketMessageNotifier ticketMessageNotifier;


    @PostConstruct
    private void registerAsObserver() {
        ticketMessageNotifier.register(this);
    }

    @Override
    @Transactional
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

    @Transactional(readOnly = true)
    public Set<Notification> findAllByUser(@NotNull Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
        return new HashSet<>(notificationRepository.findAllByUser(user));
    }

    @Transactional(readOnly = true)
    public Notification find(@NotNull Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Notification.class, id));
    }
}
