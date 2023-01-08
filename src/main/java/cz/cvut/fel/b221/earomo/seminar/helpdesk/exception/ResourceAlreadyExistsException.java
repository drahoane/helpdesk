package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import org.jetbrains.annotations.NotNull;

public class ResourceAlreadyExistsException extends HelpdeskException {

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull Long id) {
        super("Resource " + c.getSimpleName() + " #" + id + " already exists.");
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull Long id, String scope) {
        super("Resource " + c.getSimpleName() + " #" + id + " already exists. SCOPE: " + scope);
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull String identifiableBy, @NotNull String id) {
        super("Resource " + c.getSimpleName() + " with " + identifiableBy + " " + id + " already exists.");
    }
}
