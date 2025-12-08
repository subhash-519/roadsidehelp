package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.feature.auth.entity.OtpCode;
import com.roadsidehelp.api.feature.auth.repository.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class OtpTransactionService {

    private final OtpCodeRepository otpRepo;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String generateOtp(String userId) {

        // delete old OTPs for this user
        otpRepo.deleteByUserId(userId);

        // generate new 6 digit OTP
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));

        OtpCode otp = OtpCode.builder()
                .userId(userId)
                .code(code)
                .expiresAt(
                        OffsetDateTime
                                .now(ZoneId.of("Asia/Kolkata"))
                                .plusMinutes(5)
                )
                .used(false)
                .build();

        otpRepo.save(otp);

        return code;
    }
}
