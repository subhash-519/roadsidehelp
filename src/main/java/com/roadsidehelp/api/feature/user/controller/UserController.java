package com.roadsidehelp.api.feature.user.controller;

import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.user.dto.*;
import com.roadsidehelp.api.feature.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Manage profile and address")
public class UserController {

    private final UserService userService;

    // -------------------- PROFILE --------------------

    @Operation(summary = "Get logged-in user's profile",
            description = "Returns the profile details of the authenticated user")
    @ApiResponse(responseCode = "200", description = "Profile fetched successfully")
    @ApiResponse(responseCode = "404", description = "User profile not found")
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDto> getProfile() {

        String userId = CurrentUser.getUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @Operation(summary = "Update logged-in user's profile",
            description = "Updates gender, DOB, and profile image of the authenticated user")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/profile/update")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UpdateProfileRequest dto) {

        String userId = CurrentUser.getUserId();
        return ResponseEntity.ok(userService.updateProfile(userId, dto));
    }

    // -------------------- ADDRESS --------------------

    @Operation(summary = "Get logged-in user's address",
            description = "Returns saved address of authenticated user")
    @ApiResponse(responseCode = "200", description = "Address returned successfully")
    @GetMapping("/me/address")
    public ResponseEntity<UserAddressDto> getAddress() {

        String userId = CurrentUser.getUserId();
        return ResponseEntity.ok(userService.getAddress(userId));
    }

    @Operation(summary = "Update logged-in user's address",
            description = "Updates address details of the authenticated user")
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    @PutMapping("/address/update")
    public ResponseEntity<UserAddressDto> updateAddress(@RequestBody UpdateAddressRequest dto) {

        String userId = CurrentUser.getUserId();
        return ResponseEntity.ok(userService.updateAddress(userId, dto));
    }

    @GetMapping("/me")
    @Operation(summary = "Get complete logged-in user info")
    public ResponseEntity<UserFullInfoDto> getMyFullInfo() {

        String userId = CurrentUser.getUserId();
        return ResponseEntity.ok(userService.getLoggedUserFullInfo(userId));
    }

}
