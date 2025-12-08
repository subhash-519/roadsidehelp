package com.roadsidehelp.api.config.security.jwt;

import java.util.List;

public record JwtPayload(
        String userId,
        String username,
        List<String> roles
) {}
