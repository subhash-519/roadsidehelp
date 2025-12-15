package com.roadsidehelp.api.feature.garage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roadsidehelp.api.feature.garage.entity.GarageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGarageRequest {

    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @Size(max = 120)
    private String addressLine1;

    @Size(max = 120)
    private String addressLine2;

    @Size(max = 80)
    private String city;

    @Size(max = 80)
    private String state;

    @Size(max = 80)
    private String country;

    @Pattern(regexp = "\\d{6}", message = "Postal code must be 6 digits")
    private String postalCode;

    private GarageType garageType;

//    @Size(max = 20)
//    private LocalTime openingTime;
//
//    @Size(max = 20)
//    private LocalTime closingTime;

    @Schema(description = "Opening time in HH:mm", example = "09:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @Schema(description = "Closing time in HH:mm", example = "18:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @Size(max = 20)
    private String gstNumber;

    private Double latitude;

    private Double longitude;

    private String licenseDocumentUrl;
    private String gstCertificateUrl;
    private String ownerIdProofUrl;
    private String garagePhotoUrl;
    private String additionalDocUrl;

    private Boolean verified;

    @Size(max = 500)
    private String verificationReason;
}
