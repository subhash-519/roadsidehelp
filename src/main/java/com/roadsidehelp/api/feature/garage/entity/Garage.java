package com.roadsidehelp.api.feature.garage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roadsidehelp.api.core.domain.BaseEntity;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(
        name = "garage",
        indexes = {
                @Index(name = "idx_garage_name", columnList = "name"),
                @Index(name = "idx_garage_city", columnList = "city"),
                @Index(name = "idx_garage_state", columnList = "state"),
                @Index(name = "idx_garage_lat_lng", columnList = "latitude, longitude"),
                @Index(name = "idx_garage_type", columnList = "garage_type"),
                @Index(name = "idx_garage_verified", columnList = "is_verified")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garage extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private UserAccount owner;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @NotBlank
    @Size(max = 120)
    private String addressLine1;

    @Size(max = 120)
    private String addressLine2;

    @NotBlank
    @Size(max = 80)
    private String city;

    @NotBlank
    @Size(max = 80)
    private String state;

    @NotBlank
    @Size(max = 80)
    private String country;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Postal code must be 6 digits")
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "garage_type", nullable = false, length = 20)
    private GarageType garageType;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @Size(max = 20)
    private String gstNumber;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank(message = "Shop license document is required")
    private String licenseDocumentUrl;

    @NotBlank(message = "GST certificate is required")
    private String gstCertificateUrl;

    @NotBlank(message = "Owner ID proof is required")
    private String ownerIdProofUrl;

    @NotBlank(message = "Front photo of garage is required")
    private String garagePhotoUrl;

    private String additionalDocUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "garage_status", nullable = false, length = 20)
    private GarageStatus garageStatus = GarageStatus.CLOSED;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(length = 500)
    private String verificationReason;

    // ==============================
    // Convenience Methods
    // ==============================

    // Check if the garage is verified (approved by admin)
    public boolean isApproved() {
        return this.verified && this.kycStatus == KycStatus.APPROVED;
    }

    // Check if the garage is currently open based on opening/closing time
    public boolean isOpen() {
        if (this.openingTime == null || this.closingTime == null) {
            return false; // assume closed if times not set
        }

        LocalTime now = LocalTime.now();

        // Handles overnight shifts (e.g., 22:00 to 06:00)
        if (openingTime.isBefore(closingTime)) {
            return !now.isBefore(openingTime) && !now.isAfter(closingTime);
        } else {
            return !now.isBefore(openingTime) || !now.isAfter(closingTime);
        }
    }

    // Convenience method to check if garage can accept bookings
    public boolean canAcceptBooking() {
        return isApproved() && isOpen() && garageStatus == GarageStatus.OPEN;
    }
}
