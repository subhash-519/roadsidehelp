package com.roadsidehelp.api.feature.auth.dto;

public record RegisterResponse(
        String id,
        String fullName,
        String email,
        String phoneNumber,
        boolean isVerified,
        String message
) {
}
