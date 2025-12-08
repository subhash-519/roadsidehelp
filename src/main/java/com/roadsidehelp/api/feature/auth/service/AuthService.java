package com.roadsidehelp.api.feature.auth.service;

import com.roadsidehelp.api.config.exception.*;
import com.roadsidehelp.api.config.security.jwt.JwtProperties;
import com.roadsidehelp.api.config.security.jwt.JwtService;
import com.roadsidehelp.api.core.constants.TimeZones;
import com.roadsidehelp.api.feature.auth.dto.AuthResponse;
import com.roadsidehelp.api.feature.auth.dto.RegisterResponse;
import com.roadsidehelp.api.feature.auth.entity.RefreshToken;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.RefreshTokenRepository;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailVerificationService emailVerificationService;
    private final UserAccountRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final OtpAuthService otpService;


    @Transactional
    public RegisterResponse register(String fullName, String email, String phone, String password) {

        if (userRepo.existsByEmail(email)) {
            throw new ApiException(ErrorCode.ENTITY_ALREADY_EXISTS, "Email already registered");
        }

        if (userRepo.existsByPhoneNumber(phone)) {
            throw new ApiException(ErrorCode.ENTITY_ALREADY_EXISTS, "Phone already registered");
        }

        String token = UUID.randomUUID().toString();

        UserAccount user = UserAccount.builder()
                .fullName(fullName)
                .email(email)
                .phoneNumber(phone)
                .passwordHash(passwordEncoder.encode(password))
                .active(true)
                .isVerified(false)
                .verificationToken(token)
                .roles(new HashSet<>(Set.of("USER")))
                .build();

        userRepo.save(user);

        // send mail
        emailVerificationService.sendVerification(user);

        return new RegisterResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.isVerified(),
                "Account created. Please check your email to verify your account."
        );
    }

    @Transactional
    public AuthResponse login(String identifier, String rawPassword) {

        UserAccount user = userRepo.findByEmail(identifier)
                .or(() -> userRepo.findByPhoneNumber(identifier))
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Invalid credentials"));

        // *** Email verification check ***
        if (!user.isVerified()) {
            throw new ApiException(
                    ErrorCode.EMAIL_NOT_VERIFIED,
                    "Please verify your email before logging in"
            );
        }

        // Password match validation
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS, "Invalid credentials");
        }

        // Generate tokens
        String access = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refresh = jwtService.generateRefreshToken(user.getId());

        refreshRepo.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .token(refresh)
                        .expiresAt(OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
                                .plusDays(jwtProperties.getRefreshTokenValidityDays()))
                        .revoked(false)
                        .build()
        );

        return new AuthResponse(access, refresh, jwtService.getExpiration(access).toInstant());
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {

        if (!jwtService.isValidateToken(refreshToken)) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, "Invalid refresh token");
        }

        RefreshToken oldToken = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, "Refresh token not found"));

        if (oldToken.isRevoked() || oldToken.getExpiresAt().isBefore(OffsetDateTime.now(ZoneId.of(TimeZones.INDIA)))) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, "Expired or revoked refresh token");
        }

        UserAccount user = userRepo.findById(oldToken.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        oldToken.setRevoked(true);
        refreshRepo.save(oldToken);

        String newAccess = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String newRefresh = jwtService.generateRefreshToken(user.getId());

        refreshRepo.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .token(newRefresh)
                        .expiresAt(OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
                                .plusDays(jwtProperties.getRefreshTokenValidityDays()))
                        .revoked(false)
                        .build()
        );

        return new AuthResponse(newAccess, newRefresh, jwtService.getExpiration(newAccess).toInstant());
    }


    @Transactional
    public void logout(String refreshToken) {
        RefreshToken rt = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                        "Refresh token not found"));

        if (rt.isRevoked()) {
            throw new ApiException(
                    ErrorCode.REFRESH_TOKEN_ALREADY_REVOKED,
                    "Token already revoked");
        }

        rt.setRevoked(true);
        refreshRepo.save(rt);
    }

    @Transactional
    public void logoutFromAllDevices(String userId) {

        List<RefreshToken> tokens = refreshRepo.findAllByUserId(userId);

        if (tokens.isEmpty()) {
            throw new ApiException(ErrorCode.NO_ACTIVE_SESSIONS,
                    "User has no active sessions");
        }

        tokens.forEach(t -> t.setRevoked(true));
        refreshRepo.saveAll(tokens);
    }

    public AuthResponse loginWithOtp(String username, String code) {

        UserAccount user = userRepo.findByEmail(username)
                .or(() -> userRepo.findByPhoneNumber(username))
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        otpService.validateOtp(user.getId(), code);

        String access = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refresh = jwtService.generateRefreshToken(user.getId());

        return new AuthResponse(access, refresh, jwtService.getExpiration(access).toInstant());
    }

    @Transactional
    public void verifyEmail(String token) {
        UserAccount user = userRepo.findByVerificationToken(token)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Invalid token"));

        if (user.getTokenExpiration().isBefore(
                OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
        )) {
            throw new ApiException(ErrorCode.TOKEN_EXPIRED, "Verification token expired");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        userRepo.save(user);
    }

    @Transactional
    public void forgotPassword(String email) {

        UserAccount user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND,
                        "No account found with this email"
                ));

        String token = UUID.randomUUID().toString();

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(
                OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
                        .plusMinutes(15)
        );
        userRepo.save(user);

        // send email here
        emailVerificationService.sendResetPasswordEmail(user);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        UserAccount user = userRepo.findByResetPasswordToken(token)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.INVALID_TOKEN,
                        "Invalid reset token"
                ));

        if (user.getResetPasswordExpiry() == null ||
                user.getResetPasswordExpiry().isBefore(
                        OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
                )) {
            throw new ApiException(ErrorCode.TOKEN_EXPIRED, "Reset token expired");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        // cleanup
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);

        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public void validateResetToken(String token) {

        UserAccount user = userRepo.findByResetPasswordToken(token)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.INVALID_TOKEN,
                        "Invalid reset token"
                ));

        if (user.getResetPasswordExpiry() == null ||
                user.getResetPasswordExpiry().isBefore(
                        OffsetDateTime.now(ZoneId.of(TimeZones.INDIA))
                )) {
            throw new ApiException(ErrorCode.TOKEN_EXPIRED, "Reset token expired");
        }
    }
}
