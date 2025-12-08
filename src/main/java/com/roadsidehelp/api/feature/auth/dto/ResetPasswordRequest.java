package com.roadsidehelp.api.feature.auth.dto;

public record ResetPasswordRequest(
        String emailOrPhone,
        String otp,
        String newPassword
) {}
