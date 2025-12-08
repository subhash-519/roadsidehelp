package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.feature.auth.dto.ForgotPasswordRequest;
import com.roadsidehelp.api.feature.auth.dto.ResetPasswordRequest;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final OtpAuthService otpAuthService;

    public void forgotPassword(ForgotPasswordRequest request) {
        otpAuthService.sendOtp(request.emailOrPhone());
    }

    public void resetPassword(ResetPasswordRequest request) {

        UserAccount user =
                otpAuthService.verifyOtp(request.emailOrPhone(), request.otp());

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepo.save(user);
    }
}
