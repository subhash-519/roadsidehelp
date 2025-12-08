package com.roadsidehelp.api.feature.auth.controller;

import com.roadsidehelp.api.feature.auth.dto.*;
import com.roadsidehelp.api.feature.auth.entity.OtpPurpose;
import com.roadsidehelp.api.feature.auth.service.AuthService;
import com.roadsidehelp.api.feature.auth.service.OtpAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Auth APIs: Register, Login, Refresh, Logout")
public class AuthController {

    private final AuthService authService;
    private final OtpAuthService otpService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account using email, phone and password"
    )
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "User already exists")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(
                authService.register(req.fullName(), req.email(), req.phoneNumber(), req.password())
        );
    }


    @Operation(summary = "Login and get access token + refresh token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid username or password")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req.username(), req.password()));
    }


    @Operation(summary = "Refresh access token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }


    @Operation(summary = "Logout user and invalidate refresh token")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok("Logged out successfully");
    }


    @Operation(summary = "Logout from all devices")
    @ApiResponse(responseCode = "200", description = "Logged out everywhere")
    @ApiResponse(responseCode = "404", description = "No active sessions for this user")
    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutFromAllDevices(@RequestParam String userId) {
        authService.logoutFromAllDevices(userId);
        return ResponseEntity.ok("Logged out from all devices successfully");
    }

    @Operation(summary = "Send OTP to phone/email")
    @ApiResponse(responseCode = "200", description = "OTP sent successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOtp(@RequestParam String username) {
        otpService.sendOtp(username, OtpPurpose.LOGIN);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @Operation(summary = "Verify OTP and login")
    @ApiResponse(responseCode = "200", description = "OTP verified successfully")
    @ApiResponse(responseCode = "401", description = "Invalid OTP")
    @PostMapping("/otp/login")
    public ResponseEntity<AuthResponse> otpLogin(
            @RequestParam String username,
            @RequestParam String code
    ) {
        return ResponseEntity.ok(authService.loginWithOtp(username, code));
    }

    @Operation(
            summary = "Verify email",
            description = "Verify user's email using the verification token"
    )
    @ApiResponse(responseCode = "200", description = "Email verified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    @GetMapping("/verify-email")
    public ResponseEntity<String> verify(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    @Operation(
            summary = "Send password reset token",
            description = "Sends forgotten password reset token link to user's email"
    )
    @ApiResponse(responseCode = "200", description = "Password reset link sent")
    @ApiResponse(responseCode = "404", description = "User not found with this email")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Password reset link sent!");
    }

    @Operation(
            summary = "Validate reset password token",
            description = "Checks if the reset password token is valid or expired"
    )
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "400", description = "Invalid token or expired")
    @GetMapping("/password/reset")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        authService.validateResetToken(token);
        return ResponseEntity.ok("Valid reset token");
    }

    @Operation(summary = "Reset password using token")
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully!");
    }
}
