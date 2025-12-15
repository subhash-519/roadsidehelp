package com.roadsidehelp.api.feature.garage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roadsidehelp.api.feature.garage.entity.GarageStatus;
import com.roadsidehelp.api.feature.garage.entity.GarageType;
import com.roadsidehelp.api.feature.garage.entity.KycStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageResponse {

    private String id;

    private String ownerId;

    private String name;
    private String description;
    private String imageUrl;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    private GarageType garageType;

    @Schema(description = "Opening time in HH:mm", example = "09:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @Schema(description = "Closing time in HH:mm", example = "18:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
    private String gstNumber;

    private Double latitude;
    private Double longitude;

    private String licenseDocumentUrl;
    private String gstCertificateUrl;
    private String ownerIdProofUrl;
    private String garagePhotoUrl;
    private String additionalDocUrl;

    private boolean verified;
    private GarageStatus garageStatus;
    private KycStatus kycStatus;
    private String verificationReason;

    private String createdAt;
    private String updatedAt;
}
