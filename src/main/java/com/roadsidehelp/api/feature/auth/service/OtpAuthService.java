package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.OtpCode;
import com.roadsidehelp.api.feature.auth.entity.OtpPurpose;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.OtpCodeRepository;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class OtpAuthService {

    private final OtpCodeRepository otpRepo;
    private final UserAccountRepository userRepo;
    private final EmailService emailService;
    private final OtpTransactionService otpTx;

    // Send OTP to user email or phone
    public void sendOtp(String emailOrPhone, OtpPurpose purpose) {

        UserAccount user = userRepo
                .findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        String otp = otpTx.generateOtp(user.getId());

        String subject = getSubject(purpose);
        String message = getMessage(purpose, otp);

        emailService.send(
                user.getEmail(),
                subject,
                message
        );
    }


    // Verify the OTP and return the user
    public UserAccount verifyOtp(String emailOrPhone, String code) {

        UserAccount user = userRepo
                .findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        validateOtp(user.getId(), code);

        return user;
    }

    // Validate OTP
    public void validateOtp(String userId, String code) {

        OtpCode otp = otpRepo
                .findTopByUserIdAndUsedFalseOrderByExpiresAtDesc(userId)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.OTP_INVALID, "OTP not found"));

        // Wrong OTP
        if (!otp.getCode().equals(code)) {
            throw new ApiException(ErrorCode.OTP_INVALID, "Invalid OTP");
        }

        // Expired?
        if (otp.getExpiresAt().isBefore(
                OffsetDateTime.now(ZoneId.of("Asia/Kolkata"))
        )) {
            throw new ApiException(ErrorCode.OTP_INVALID, "OTP expired");
        }

        // Mark as used
        otp.setUsed(true);
        otpRepo.save(otp);
    }

    private String getSubject(OtpPurpose purpose) {
        return switch (purpose) {
            case LOGIN -> "Login OTP";
            case PASSWORD_RESET -> "Password Reset OTP";
            case EMAIL_VERIFICATION -> "Email Verification OTP";
        };
    }

    private String getMessage(OtpPurpose purpose, String otp) {
        return switch (purpose) {
            case LOGIN -> "Your OTP to login is: " + otp;
            case PASSWORD_RESET -> "Your password reset OTP is: " + otp;
            case EMAIL_VERIFICATION -> "Your email verification OTP is: " + otp;
        };
    }

}
