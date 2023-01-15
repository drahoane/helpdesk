package cz.cvut.fel.b221.earomo.seminar.helpdesk.observer;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.TicketMessage;
import org.springframework.stereotype.Component;

@Component
public class TicketMessageNotifier extends Notifier {
    public void newTicketMessage(TicketMessage message) {
        notifyObservers(message);
    }
}
