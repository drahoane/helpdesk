package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import org.jetbrains.annotations.NotNull;

public class IllegaleStateChangeException extends RuntimeException {
    public IllegaleStateChangeException() {
        super();
    }

    public IllegaleStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState, @NotNull Class futureState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to " + futureState.getSimpleName());
    }

    public IllegaleStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to undefined state.");
    }
}
