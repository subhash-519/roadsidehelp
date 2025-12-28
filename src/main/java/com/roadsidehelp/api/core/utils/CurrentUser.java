package com.roadsidehelp.api.core.utils;

import com.roadsidehelp.api.feature.auth.config.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    private CurrentUser(){}

    public static String getUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null)
            return null;

        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getUserId();
        }

        return null;
    }

    public static String getEmail() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null)
            return null;

        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getEmail();
        }

        return null;
    }
}
