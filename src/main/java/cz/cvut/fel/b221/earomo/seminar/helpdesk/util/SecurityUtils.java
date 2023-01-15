package cz.cvut.fel.b221.earomo.seminar.helpdesk.util;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.SecurityUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class SecurityUtils {
    public static SecurityUser getCurrentUser() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();

        assert securityContextHolder != null;
        return (SecurityUser) securityContextHolder.getAuthentication().getPrincipal();
    }

    public static String getCurrentUserIp() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();

        assert securityContextHolder != null;
        assert securityContextHolder.getAuthentication().getDetails() instanceof WebAuthenticationDetails;
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) securityContextHolder.getAuthentication().getDetails();

        return webAuthenticationDetails.getRemoteAddress();
    }
}
