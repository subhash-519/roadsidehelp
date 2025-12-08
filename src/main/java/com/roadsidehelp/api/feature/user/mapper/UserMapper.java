package com.roadsidehelp.api.feature.user.mapper;

import com.roadsidehelp.api.feature.user.dto.*;
import com.roadsidehelp.api.feature.user.entity.*;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;

public final class UserMapper {

    private UserMapper() {}

    // -----------------------------------------------------
    // USER PROFILE → DTO
    // -----------------------------------------------------
    public static UserProfileDto toDto(UserProfile profile) {
        if (profile == null) return null;

        UserAccount user = profile.getUser();

        return new UserProfileDto(
                user != null ? user.getId() : null,
                user != null ? user.getFullName() : null,
                user != null ? user.getEmail() : null,
                user != null ? user.getPhoneNumber() : null,
                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getProfileImageUrl()
        );
    }

    // -----------------------------------------------------
    // USER ADDRESS → DTO  (UPDATED)
    // -----------------------------------------------------
    public static UserAddressDto toDto(UserAddress address) {
        if (address == null) return null;

        UserAccount user = address.getUser();

        return new UserAddressDto(
                user != null ? user.getId() : null,
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );
    }

    // -----------------------------------------------------
    // UPDATE PROFILE ENTITY
    // -----------------------------------------------------
    public static void applyUpdateProfile(UserProfile profile, UpdateProfileRequest req, UserAccount user) {
        if (profile == null || req == null) return;

        profile.setUser(user);
        profile.setGender(req.getGender());
        profile.setDateOfBirth(req.getDateOfBirth());
        profile.setProfileImageUrl(req.getProfileImageUrl());
    }

    // -----------------------------------------------------
    // UPDATE ADDRESS ENTITY (UPDATED)
    // -----------------------------------------------------
    public static void applyUpdateAddress(UserAddress address, UpdateAddressRequest req, UserAccount user) {
        if (address == null || req == null) return;

        address.setUser(user);
        address.setAddressLine1(req.getAddressLine1());
        address.setAddressLine2(req.getAddressLine2());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
    }
}
