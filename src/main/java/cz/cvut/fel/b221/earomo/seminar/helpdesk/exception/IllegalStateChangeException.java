package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class IllegalStateChangeException extends HelpdeskException {
    public IllegalStateChangeException() {
        super();
    }

    public IllegalStateChangeException(String msg) {
        super(msg);
        log.info("IllegalStateChangeException has been thrown");
    }

    public IllegalStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState, @NotNull Class futureState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to " + futureState.getSimpleName());
        log.info("IllegalStateChangeException using future state has been thrown");
    }

    public IllegalStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to undefined state.");
        log.info("IllegalStateChangeException using current state has been thrown");
    }
}
