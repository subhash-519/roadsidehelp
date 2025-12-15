package com.roadsidehelp.api.feature.garage.mapper;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.garage.dto.CreateGarageRequest;
import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
import com.roadsidehelp.api.feature.garage.dto.UpdateGarageRequest;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import org.springframework.stereotype.Component;

@Component
public class GarageMapper {

    // CreateGarageRequest → Garage
    public Garage toEntity(CreateGarageRequest req, UserAccount owner) {
        if (req == null) return null;

        return Garage.builder()
                .owner(owner)
                .name(req.getName())
                .description(req.getDescription())
                .imageUrl(req.getImageUrl())
                .addressLine1(req.getAddressLine1())
                .addressLine2(req.getAddressLine2())
                .city(req.getCity())
                .state(req.getState())
                .country(req.getCountry())
                .postalCode(req.getPostalCode())
                .garageType(req.getGarageType())
                .openingTime(req.getOpeningTime())
                .closingTime(req.getClosingTime())
                .gstNumber(req.getGstNumber())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .licenseDocumentUrl(req.getLicenseDocumentUrl())
                .gstCertificateUrl(req.getGstCertificateUrl())
                .ownerIdProofUrl(req.getOwnerIdProofUrl())
                .garagePhotoUrl(req.getGaragePhotoUrl())
                .additionalDocUrl(req.getAdditionalDocUrl() != null ? req.getAdditionalDocUrl() : "")
                .verified(false)
                .verificationReason(null)
                .build();
    }

    // UpdateGarageRequest → existing Garage
    public void updateEntity(Garage garage, UpdateGarageRequest req) {
        if (garage == null || req == null) return;

        applyIfNotNull(req.getName(), garage::setName);
        applyIfNotNull(req.getDescription(), garage::setDescription);
        applyIfNotNull(req.getImageUrl(), garage::setImageUrl);
        applyIfNotNull(req.getAddressLine1(), garage::setAddressLine1);
        applyIfNotNull(req.getAddressLine2(), garage::setAddressLine2);
        applyIfNotNull(req.getCity(), garage::setCity);
        applyIfNotNull(req.getState(), garage::setState);
        applyIfNotNull(req.getCountry(), garage::setCountry);
        applyIfNotNull(req.getPostalCode(), garage::setPostalCode);
        applyIfNotNull(req.getGarageType(), garage::setGarageType);
        applyIfNotNull(req.getOpeningTime(), garage::setOpeningTime);
        applyIfNotNull(req.getClosingTime(), garage::setClosingTime);
        applyIfNotNull(req.getGstNumber(), garage::setGstNumber);
        applyIfNotNull(req.getLatitude(), garage::setLatitude);
        applyIfNotNull(req.getLongitude(), garage::setLongitude);
        applyIfNotNull(req.getLicenseDocumentUrl(), garage::setLicenseDocumentUrl);
        applyIfNotNull(req.getGstCertificateUrl(), garage::setGstCertificateUrl);
        applyIfNotNull(req.getOwnerIdProofUrl(), garage::setOwnerIdProofUrl);
        applyIfNotNull(req.getGaragePhotoUrl(), garage::setGaragePhotoUrl);
        applyIfNotNull(req.getAdditionalDocUrl(), garage::setAdditionalDocUrl);
        applyIfNotNull(req.getVerified(), garage::setVerified);
        applyIfNotNull(req.getVerificationReason(), garage::setVerificationReason);
    }

    private <T> void applyIfNotNull(T value, java.util.function.Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }

    // Garage → GarageResponse
    public GarageResponse toResponse(Garage g) {
        if (g == null) return null;

        return GarageResponse.builder()
                .id(g.getId())
                .ownerId(g.getOwner() != null ? g.getOwner().getId() : null)
                .name(g.getName())
                .description(g.getDescription())
                .imageUrl(g.getImageUrl())
                .addressLine1(g.getAddressLine1())
                .addressLine2(g.getAddressLine2())
                .city(g.getCity())
                .state(g.getState())
                .country(g.getCountry())
                .postalCode(g.getPostalCode())
                .garageType(g.getGarageType())
                .openingTime(g.getOpeningTime())
                .closingTime(g.getClosingTime())
                .gstNumber(g.getGstNumber())
                .latitude(g.getLatitude())
                .longitude(g.getLongitude())
                .licenseDocumentUrl(g.getLicenseDocumentUrl())
                .gstCertificateUrl(g.getGstCertificateUrl())
                .ownerIdProofUrl(g.getOwnerIdProofUrl())
                .garagePhotoUrl(g.getGaragePhotoUrl())
                .additionalDocUrl(g.getAdditionalDocUrl())
                .verified(g.isVerified())
                .verificationReason(g.getVerificationReason())
                .garageStatus(g.getGarageStatus())
                .kycStatus(g.getKycStatus())
                .createdAt(g.getCreatedAt() != null ? g.getCreatedAt().toString() : null)
                .updatedAt(g.getUpdatedAt() != null ? g.getUpdatedAt().toString() : null)
                .build();
    }
}
