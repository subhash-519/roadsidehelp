package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.garage.dto.*;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.entity.GarageStatus;
import com.roadsidehelp.api.feature.garage.entity.KycStatus;
import com.roadsidehelp.api.feature.garage.mapper.GarageMapper;
import com.roadsidehelp.api.feature.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerGarageServiceImpl implements OwnerGarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;
    private final UserAccountRepository userAccountRepository;

    private static final String GARAGE_NOT_FOUND = "Garage not found";

    // ===================== CREATE GARAGE =====================
    @Transactional
    @Override
    public GarageResponse createGarage(String ownerId, CreateGarageRequest req) {

        if (garageRepository.findByOwner_Id(ownerId).isPresent()) {
            throw new ApiException(
                    ErrorCode.ENTITY_ALREADY_EXISTS,
                    "Garage already exists for this user"
            );
        }

        UserAccount owner = userAccountRepository.findById(ownerId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND, "User not found"
                ));

        Garage garage = garageMapper.toEntity(req, owner);
        garage.setGarageStatus(GarageStatus.CLOSED);
        garage.setKycStatus(KycStatus.PENDING);
        garage.setVerified(false);

        garageRepository.save(garage);
        return garageMapper.toResponse(garage);
    }

    // ===================== GET MY GARAGE =====================
    @Override
    public GarageResponse getMyGarage(String ownerId) {

        Garage garage = garageRepository.findByOwner_Id(ownerId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND,
                        "Garage not found for logged-in user"
                ));

        return garageMapper.toResponse(garage);
    }

    // ===================== UPDATE GARAGE =====================
    @Transactional
    @Override
    public GarageResponse updateGarage(String ownerId, UpdateGarageRequest req) {

        Garage garage = garageRepository.findByOwner_Id(ownerId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, GARAGE_NOT_FOUND
                ));

        garage.setVerified(false);
        garage.setKycStatus(KycStatus.PENDING);

        garageMapper.updateEntity(garage, req);
        return garageMapper.toResponse(garage);
    }

    // ===================== UPDATE DOCUMENTS =====================
    @Transactional
    @Override
    public GarageResponse updateDocuments(String ownerId, GarageDocumentRequest req) {

        Garage garage = garageRepository.findByOwner_Id(ownerId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, GARAGE_NOT_FOUND
                ));

        garage.setLicenseDocumentUrl(req.getLicenseDocumentUrl());
        garage.setGstCertificateUrl(req.getGstCertificateUrl());
        garage.setOwnerIdProofUrl(req.getOwnerIdProofUrl());
        garage.setGaragePhotoUrl(req.getGaragePhotoUrl());

        garage.setVerified(false);
        garage.setKycStatus(KycStatus.PENDING);

        return garageMapper.toResponse(garage);
    }

    // ===================== OPEN / CLOSE GARAGE =====================
    @Transactional
    @Override
    public GarageResponse updateOpenStatus(String ownerId, boolean open) {

        Garage garage = garageRepository.findByOwner_Id(ownerId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, GARAGE_NOT_FOUND
                ));

        if (open && (!garage.isVerified() || garage.getKycStatus() != KycStatus.APPROVED)) {
            throw new ApiException(
                    ErrorCode.ACCESS_DENIED,
                    "Garage must be verified and KYC approved before opening"
            );
        }

        garage.setGarageStatus(open ? GarageStatus.OPEN : GarageStatus.CLOSED);
        return garageMapper.toResponse(garage);
    }

    @Override
    public GarageOwnerStatusResponse getMyGarageStatus(String ownerId) {

        return garageRepository.findByOwner_Id(ownerId)
                .map(garage -> new GarageOwnerStatusResponse(
                        true,
                        garage.getKycStatus(),
                        garage.isVerified(),
                        resolveMessage(garage)
                ))
                .orElseGet(() -> new GarageOwnerStatusResponse(
                        false,
                        null,
                        false,
                        "You have not applied for a garage"
                ));
    }

    private String resolveMessage(Garage garage) {
        return switch (garage.getKycStatus()) {
            case PENDING -> "Your garage application is under review";
            case REJECTED -> "Garage rejected: " + garage.getVerificationReason();
            case APPROVED -> "Garage approved";
            default -> "Garage status unavailable";
        };
    }
}
