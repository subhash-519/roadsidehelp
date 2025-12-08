package com.roadsidehelp.api.feature.user.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.feature.user.dto.*;
import com.roadsidehelp.api.feature.user.entity.*;
import com.roadsidehelp.api.feature.user.mapper.UserMapper;
import com.roadsidehelp.api.feature.user.repository.*;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final UserAddressRepository addressRepo;

    public static final String USER_NOT_FOUND = "User not found";

    @Override
    public UserProfileDto getProfile(String userId) {

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, USER_NOT_FOUND));

        var profile = profileRepo.findByUser(user)
                .orElse(new UserProfile()); // return empty profile instead of error

        return UserMapper.toDto(profile);
    }

    @Override
    public UserProfileDto updateProfile(String userId, UpdateProfileRequest dto) {

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, USER_NOT_FOUND));

        var profile = profileRepo.findByUser(user)
                .orElse(new UserProfile());

        profile.setUser(user);
        profile.setGender(dto.getGender());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setProfileImageUrl(dto.getProfileImageUrl());

        profileRepo.save(profile);

        return UserMapper.toDto(profile);
    }

    @Override
    public UserAddressDto getAddress(String userId) {

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, USER_NOT_FOUND));

        var address = addressRepo.findByUser(user)
                .orElse(new UserAddress()); // return empty address

        return UserMapper.toDto(address);
    }

    @Override
    public UserAddressDto updateAddress(String userId, UpdateAddressRequest dto) {

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, USER_NOT_FOUND));

        var address = addressRepo.findByUser(user)
                .orElse(new UserAddress());

        address.setUser(user);
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());

        addressRepo.save(address);

        return UserMapper.toDto(address);
    }

    @Override
    public UserFullInfoDto getLoggedUserFullInfo(String userId) {

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, USER_NOT_FOUND));

        var profile = profileRepo.findByUser(user).orElse(new UserProfile());
        var address = addressRepo.findByUser(user).orElse(new UserAddress());

        return new UserFullInfoDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),

                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getProfileImageUrl(),

                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );
    }
}
