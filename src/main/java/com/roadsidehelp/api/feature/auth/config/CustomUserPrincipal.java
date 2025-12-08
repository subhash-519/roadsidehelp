package com.roadsidehelp.api.feature.auth.config;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserPrincipal implements UserDetails {

    private final String userId;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public CustomUserPrincipal(
            String userId,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean active
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    public static CustomUserPrincipal from(UserAccount user, Collection<? extends GrantedAuthority> authorities) {
        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                authorities,
                user.isActive()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // VERY IMPORTANT → THIS RETURNS USER ID (same as JWT subject)
    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return active; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }
}
