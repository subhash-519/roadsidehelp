package com.roadsidehelp.api.feature.auth.dto;

public record RegisterRequest(
        String fullName,
        String email,
        String phoneNumber,
        String password
) {}
