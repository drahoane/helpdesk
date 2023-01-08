package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.Role;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.UserType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        if(user.getUserType() == UserType.CUSTOMER) {
            roles.add(new SimpleGrantedAuthority(Role.CUSTOMER.getName()));
        } else if(user.getUserType() == UserType.EMPLOYEE) {
            roles.add(new SimpleGrantedAuthority(Role.EMPLOYEE.getName()));
        } else if(user.getUserType() == UserType.MANAGER) {
            roles.add(new SimpleGrantedAuthority(Role.MANAGER.getName()));
        } else {
            throw new RuntimeException("Cannot assign role. Unexpected UserType.");
        }

        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isAccountDisabled();
    }

    public User getUser() {
        return user;
    }

    public boolean hasRole(Role role) {
        return getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(role.getName()));
    }

    public boolean hasAnyRole(Role... roles) {
        for(Role role : roles) {
            if(hasRole(role))
                return true;
        }

        return false;
    }

    public boolean isAssignedToTicket(Ticket ticket) {
        return isEmployee() && ticket.getAssignedEmployees().stream().anyMatch(x -> x.getUserId().equals(this.user.getUserId()));
    }

    public boolean ownsTicket(Ticket ticket) {
        return isCustomer() && user.getUserId().equals(ticket.getOwner().getUserId());
    }

    public boolean isCustomer() {
        return user.getUserType().equals(UserType.CUSTOMER);
    }

    public boolean isEmployee() {
        return user.getUserType().equals(UserType.EMPLOYEE);
    }

    public boolean isManager() {
        return user.getUserType().equals(UserType.MANAGER);
    }
}
