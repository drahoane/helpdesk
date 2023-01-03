package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import org.jetbrains.annotations.NotNull;

public class IllegalStateChangeException extends HelpdeskException {
    public IllegalStateChangeException() {
        super();
    }

    public IllegalStateChangeException(String msg) {
        super(msg);
    }

    public IllegalStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState, @NotNull Class futureState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to " + futureState.getSimpleName());
    }

    public IllegalStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to undefined state.");
    }
}
