package com.roadsidehelp.api.feature.user.service;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.user.dto.*;

public interface UserService {

    UserProfileDto getProfile(String userId);

    UserProfileDto updateProfile(String userId, UpdateProfileRequest dto);

    UserAddressDto getAddress(String userId);

    UserAddressDto updateAddress(String userId, UpdateAddressRequest dto);

    UserFullInfoDto getLoggedUserFullInfo(String userId);

    UserAccount getUserEntity(String userId);

}
