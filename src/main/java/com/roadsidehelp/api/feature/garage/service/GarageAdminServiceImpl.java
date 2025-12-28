package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.entity.UserRole;
import com.roadsidehelp.api.feature.auth.entity.UserType;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.entity.KycStatus;
import com.roadsidehelp.api.feature.garage.mapper.GarageMapper;
import com.roadsidehelp.api.feature.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GarageAdminServiceImpl implements GarageAdminService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;
    private final UserAccountRepository userAccountRepository;
    private final GarageEmailNotificationService garageEmailNotificationService;

    @Override
    public List<GarageResponse> getPendingGarages() {
        return garageRepository
                .findByKycStatus(KycStatus.PENDING)
                .stream()
                .map(garageMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public GarageResponse approveGarage(String garageId) {
        Garage garage = getGarageOrThrow(garageId);

        garage.setKycStatus(KycStatus.APPROVED);
        garage.setVerified(true);
        garage.setVerificationReason(null);

        UserAccount owner = garage.getOwner();
        owner.getRoles().remove(UserRole.USER);
        owner.getRoles().add(UserRole.GARAGE);
        owner.setUserType(UserType.GARAGE);

        userAccountRepository.save(owner);
        garageRepository.save(garage);

        garageEmailNotificationService.sendApprovalNotification(owner);
        return garageMapper.toResponse(garage);
    }

    @Override
    public GarageResponse rejectGarage(String garageId, String reason) {
        Garage garage = getGarageOrThrow(garageId);

        garage.setKycStatus(KycStatus.REJECTED);
        garage.setVerified(false);
        garage.setVerificationReason(reason);

        return garageMapper.toResponse(garage);
    }

    @Override
    public List<GarageResponse> getAllGarages() {
        return garageRepository.findAll()
                .stream()
                .map(garageMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteGarage(String garageId) {
        Garage garage = getGarageOrThrow(garageId);
        garageRepository.delete(garage);
    }

    private Garage getGarageOrThrow(String id) {
        return garageRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.USER_NOT_FOUND, "Garage not found"));
    }
}
