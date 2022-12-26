package cz.cvut.fel.b221.earomo.seminar.helpdesk.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
}
