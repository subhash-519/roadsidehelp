package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.auth.config.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepo;

    // LOGIN using email OR phone
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepo.findByEmail(username)
                .or(() -> userRepo.findByPhoneNumber(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return buildUserPrincipal(user);
    }

    // JWT filter uses userId
    public UserDetails loadUserById(String userId) {
        UserAccount user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return buildUserPrincipal(user);
    }

    private UserDetails buildUserPrincipal(UserAccount user) {
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
        return CustomUserPrincipal.from(user, authorities);
    }
}
