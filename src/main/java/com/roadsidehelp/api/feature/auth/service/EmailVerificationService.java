package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public String generateToken(UserAccount user){
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        return token;
    }

    public void sendVerification(UserAccount user){
        String token = generateToken(user);
        userRepo.save(user);

        emailService.send(
                user.getEmail(),
                "Verify your account",
                "Click here to verify: http://localhost:8080/api/v1/auth/email/verify?token=" + token
        );
    }

    public void sendResetPasswordEmail(UserAccount user) {

        String link = "http://localhost:8080/api/v1/auth/password/reset?token="
                + user.getResetPasswordToken();

        emailService.send(
                user.getEmail(),
                "Reset your password",
                "Click here to reset your password: " + link
        );
    }


}
