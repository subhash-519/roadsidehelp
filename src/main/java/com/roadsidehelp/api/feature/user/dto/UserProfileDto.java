package com.roadsidehelp.api.feature.user.dto;

import java.time.LocalDate;

public record UserProfileDto(
        String userId,
        String fullName,
        String email,
        String phoneNumber,
        String gender,
        LocalDate dateOfBirth,
        String profileImageUrl
) {}
