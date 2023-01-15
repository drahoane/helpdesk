package cz.cvut.fel.b221.earomo.seminar.helpdesk.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ResourceNotFoundException extends HelpdeskException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull Long id) {
        super("Resource " + c.getSimpleName() + " #" + id + " not found.");
        log.info("ResourceNotFoundException has been thrown");
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull Long id, String scope) {
        super("Resource " + c.getSimpleName() + " #" + id + " not found. SCOPE: " + scope);
        log.info("ResourceNotFoundException using scope has been thrown");
    }

    public ResourceNotFoundException(@NotNull Class c, @NotNull String identifiableBy, @NotNull String id) {
        super("Resource " + c.getSimpleName() + " with " + identifiableBy + " " + id + " not found.");
        log.info("ResourceNotFoundException using identifiableBy has been thrown");
    }
}
