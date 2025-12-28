package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MechanicEmailVerificationServiceImpl
        implements MechanicEmailVerificationService {

    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;

    // ===================== SEND VERIFICATION =====================
    @Override
    public void sendVerification(UserAccount userAccount, String tempPassword) {

        String verificationLink =
                "http://localhost:8080/api/v1/mechanic/auth/verify-email?token="
                        + userAccount.getVerificationToken();

        String body = """
                Hello %s,

                Your mechanic account has been created.

                Temporary Password: %s

                Please verify your email using the link below:
                %s

                Regards,
                Roadside Help Team
                """.formatted(
                userAccount.getFullName(),
                tempPassword,
                verificationLink
        );

        emailService.send(
                userAccount.getEmail(),
                "Verify your mechanic account",
                body
        );
    }

    // ===================== VERIFY EMAIL =====================
    @Override
    @Transactional
    public void verifyEmail(String token) {

        UserAccount userAccount = userAccountRepository
                .findByVerificationToken(token)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.INVALID_REQUEST,
                        "Invalid or expired verification token"
                ));

        userAccount.setVerified(true);
        userAccount.setVerificationToken(null);
        userAccount.setTokenExpiration(null);
    }
}
