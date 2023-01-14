package cz.cvut.fel.b221.earomo.seminar.helpdesk.observer;

import java.util.HashSet;
import java.util.Set;

public abstract class Notifier {
    Set<Observer> observers = new HashSet<>();

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void unregister(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObservers(Object obj) {
        for(Observer o : observers) {
            o.update(obj);
        }
    }
}
