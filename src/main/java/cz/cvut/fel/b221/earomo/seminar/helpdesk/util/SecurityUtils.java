package cz.cvut.fel.b221.earomo.seminar.helpdesk.util;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static SecurityUser getCurrentUser() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();

        assert securityContextHolder != null;
        return (SecurityUser) securityContextHolder.getAuthentication().getPrincipal();
    }
}
