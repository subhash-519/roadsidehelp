package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.OtpCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.OtpCodeRepository;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpAuthService {

    private final OtpCodeRepository otpRepo;
    private final UserAccountRepository userRepo;
    private final EmailService emailService;

    /**
     * Send OTP using email
     */
    public void sendOtp(String emailOrPhone) {
        UserAccount user = userRepo
                .findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        String otp = generateOtp(user.getId());

        // NOW WE SEND EMAIL
        emailService.send(
                user.getEmail(),
                "Reset Password OTP",
                "Your OTP is: " + otp
        );
    }

    /**
     * Returns the user after verification
     */
    public UserAccount verifyOtp(String emailOrPhone, String code) {

        UserAccount user = userRepo
                .findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        validateOtp(user.getId(), code);

        return user;
    }

    public String generateOtp(String userId) {

        otpRepo.deleteByUserId(userId); // remove old OTP

        String code = String.format("%06d", new Random().nextInt(1000000));

        OtpCode otp = OtpCode.builder()
                .userId(userId)
                .code(code)
                .expiresAt(OffsetDateTime.now(ZoneId.of("Asia/Kolkata")).plusMinutes(5))
                .used(false)
                .build();

        otpRepo.save(otp);
        return code;
    }

    public void validateOtp(String userId, String code) {

        OtpCode otp = otpRepo
                .findTopByUserIdAndUsedFalseOrderByExpiresAtDesc(userId)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.OTP_INVALID, "OTP not found"));

        if (!otp.getCode().equals(code)) {
            throw new ApiException(ErrorCode.OTP_INVALID, "Invalid OTP");
        }

        if (otp.getExpiresAt().isBefore(OffsetDateTime.now(ZoneId.of("Asia/Kolkata")))) {
            throw new ApiException(ErrorCode.OTP_INVALID, "OTP expired");
        }

        otp.setUsed(true);
        otpRepo.save(otp);
    }
}
