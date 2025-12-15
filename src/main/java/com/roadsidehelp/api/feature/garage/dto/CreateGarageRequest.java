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
public class CreateGarageRequest {

    @NotBlank
    @Size(max = 120)
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

    @NotNull(message = "Garage type is required")
    private GarageType garageType; // BIKE, CAR, BOTH

//    @NotNull(message = "Opening time is required")
//    @JsonFormat(pattern = "HH:mm")
//    private LocalTime openingTime; // 09:00
//
//    @NotNull(message = "Closing time is required")
//    @JsonFormat(pattern = "HH:mm")
//    private LocalTime closingTime;  // 18:00

    @Schema(description = "Opening time in HH:mm", example = "09:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @Schema(description = "Closing time in HH:mm", example = "18:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @Size(max = 20)
    private String gstNumber;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotBlank
    private String licenseDocumentUrl;

    @NotBlank
    private String gstCertificateUrl;

    @NotBlank
    private String ownerIdProofUrl;

    @NotBlank
    private String garagePhotoUrl;

    private String additionalDocUrl;
}
