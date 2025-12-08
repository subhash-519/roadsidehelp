package com.roadsidehelp.api.feature.auth.entity;

import com.roadsidehelp.api.core.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_phone", columnList = "phone_number", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount extends BaseEntity {

    @NotBlank
    @Column(name = "full_name", length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format"
    )
    @Column(name = "email", length = 150, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password_hash", length = 256)
    private String passwordHash;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+91|0)?[6-9]\\d{9}$",
            message = "Invalid phone number. Use 10-digit or +91 format"
    )
    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "token_expiration")
    private OffsetDateTime tokenExpiration;

    @Column(name = "is_verified")
    private boolean isVerified;

    // ----------------------
    // Added for Forgot password
    // ----------------------

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_password_expiry")
    private OffsetDateTime resetPasswordExpiry;
}
