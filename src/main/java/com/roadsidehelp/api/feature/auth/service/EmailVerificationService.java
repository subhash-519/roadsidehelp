package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UserAccountRepository userRepo;
    private final EmailService emailService;

    public void verifyEmail(String token){
        UserAccount user = userRepo.findByVerificationToken(token)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Invalid verification token"));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepo.save(user);
    }

    public void sendVerification(UserAccount user) {
        emailService.send(
                user.getEmail(),
                "Verify your account",
                "Click here to verify: http://15.207.82.163:30007/api/v1/auth/verify-email?token="
                        + user.getVerificationToken()
        );
    }

    public void sendResetPasswordEmail(UserAccount user) {

        String link = "http://localhost:5173/update-password?token="
                + user.getResetPasswordToken();

        emailService.send(
                user.getEmail(),
                "Reset your password",
                "Click here to reset your password: " + link
        );
    }
}
