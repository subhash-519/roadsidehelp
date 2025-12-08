package com.roadsidehelp.api.feature.user.dto;

import java.time.LocalDate;

public record UserFullInfoDto(
        String userId,

        // Account info
        String fullName,
        String email,
        String phoneNumber,

        // Profile info
        String gender,
        LocalDate dateOfBirth,
        String profileImageUrl,

        // Address info
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode
) {}
