package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ResourceAlreadyExistsException extends HelpdeskException {

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull Long id) {
        super("Resource " + c.getSimpleName() + " #" + id + " already exists.");
        log.info("ResourceAlreadyExistsException has been thrown");
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull Long id, String scope) {
        super("Resource " + c.getSimpleName() + " #" + id + " already exists. SCOPE: " + scope);
        log.info("ResourceAlreadyExistsException using scope has been thrown");
    }

    public ResourceAlreadyExistsException(@NotNull Class c, @NotNull String identifiableBy, @NotNull String id) {
        super("Resource " + c.getSimpleName() + " with " + identifiableBy + " " + id + " already exists.");
        log.info("ResourceAlreadyExistsException using identifiableBy has been thrown");
    }
}
