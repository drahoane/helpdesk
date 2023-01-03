package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class IllegaleStateChangeException extends RuntimeException {
    public IllegaleStateChangeException() {
        super();
    }

    public IllegaleStateChangeException(String msg) {
        super(msg);
    }

    public IllegaleStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState, @NotNull Class futureState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to " + futureState.getSimpleName());
    }

    public IllegaleStateChangeException(@NotNull Class c, @NotNull Long id, @NotNull Class currentState) {
        super("State for resource " + c.getSimpleName() + " #" + id + " can not be changed from " + currentState.getSimpleName() + " to undefined state.");
    }
}
