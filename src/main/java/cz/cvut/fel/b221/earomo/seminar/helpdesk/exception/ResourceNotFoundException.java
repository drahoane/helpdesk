package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import org.jetbrains.annotations.NotNull;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends HelpdeskException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull Long id) {
        super("Resource " + c.getSimpleName() + " #" + id + " not found.");
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull Long id, String scope) {
        super("Resource " + c.getSimpleName() + " #" + id + " not found. SCOPE: " + scope);
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull String identifiableBy, @NotNull String id) {
        super("Resource " + c.getSimpleName() + " with " + identifiableBy + " " + id + " not found.");
    }
}
