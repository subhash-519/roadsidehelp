package com.roadsidehelp.api.feature.auth.dto;

public record ForgotPasswordRequest(
        String emailOrPhone
) {}
