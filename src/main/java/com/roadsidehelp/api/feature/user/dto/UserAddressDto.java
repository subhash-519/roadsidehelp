package com.roadsidehelp.api.feature.user.dto;

public record UserAddressDto(
        String userId,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode
) {}
