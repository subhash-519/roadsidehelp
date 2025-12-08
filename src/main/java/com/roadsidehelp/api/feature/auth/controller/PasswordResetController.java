package com.roadsidehelp.api.feature.auth.controller;

import com.roadsidehelp.api.feature.auth.dto.ForgotPasswordRequest;
import com.roadsidehelp.api.feature.auth.dto.ResetPasswordRequest;
import com.roadsidehelp.api.feature.auth.service.EmailVerificationService;
import com.roadsidehelp.api.feature.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request);
        return "OTP sent";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return "Password updated";
    }

    @GetMapping("/email/verify")
    public String verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        return "Email verified successfully";
    }
}
