package com.roadsidehelp.api.feature.auth.dto;

public record AuthRequest(
        String username,
        String password
) {}
