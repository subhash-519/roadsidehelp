package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.entity.UserRole;
import com.roadsidehelp.api.feature.auth.entity.UserType;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.repository.GarageRepository;
import com.roadsidehelp.api.feature.mechanic.dto.CreateMechanicRequest;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.dto.UpdateMechanicStatusRequest;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.entity.MechanicStatus;
import com.roadsidehelp.api.feature.mechanic.mapper.MechanicMapper;
import com.roadsidehelp.api.feature.mechanic.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicGarageServiceImpl implements MechanicGarageService {

    private final PasswordEncoder passwordEncoder;
    private final GarageRepository garageRepository;
    private final MechanicRepository mechanicRepository;
    private final UserAccountRepository userAccountRepository;
    private final MechanicEmailVerificationService mechanicEmailVerificationService;

    @Override
    public MechanicResponse createMechanic(
            String userAccountId,
            CreateMechanicRequest request
    ) {

        // FETCH GARAGE BY OWNER
        Garage garage = garageRepository.findByOwner_Id(userAccountId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found"
                ));

        // UNIQUENESS CHECK
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(
                    ErrorCode.ENTITY_ALREADY_EXISTS, "Email already exists"
            );
        }

        if (userAccountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ApiException(
                    ErrorCode.ENTITY_ALREADY_EXISTS, "Phone number already exists"
            );
        }

        // SECURITY DATA
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String verificationToken = UUID.randomUUID().toString();

        // CREATE USER ACCOUNT
        UserAccount userAccount = UserAccount.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(tempPassword))
                .roles(Set.of(UserRole.MECHANIC))
                .userType(UserType.MECHANIC)
                .active(true)
                .isVerified(false)
                .verificationToken(verificationToken)
                .tokenExpiration(null)
                .build();

        userAccountRepository.save(userAccount);

        // CREATE MECHANIC
        Mechanic mechanic = Mechanic.builder()
                .userAccount(userAccount)
                .garage(garage)
                .status(MechanicStatus.INACTIVE)
                .available(true)
                .firstLogin(true)
                .build();

        mechanicRepository.save(mechanic);

        // SEND VERIFICATION EMAIL
        mechanicEmailVerificationService.sendVerification(
                userAccount,
                tempPassword
        );

        return MechanicMapper.toResponse(mechanic);
    }

    // ===================== GET GARAGE MECHANICS =====================
    @Override
    public List<MechanicResponse> getGarageMechanics(String userAccountId) {

        Garage garage = garageRepository.findByOwner_Id(userAccountId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found for logged-in user"
                ));

        return mechanicRepository.findByGarageId(garage.getId())
                .stream()
                .map(MechanicMapper::toResponse)
                .toList();
    }

    // ===================== UPDATE MECHANIC STATUS =====================
    @Override
    public MechanicResponse updateMechanicStatus(
            String userAccountId,
            String mechanicId,
            UpdateMechanicStatusRequest request
    ) {

        Garage garage = garageRepository.findByOwner_Id(userAccountId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found for logged-in user"
                ));

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"
                ));

        if (!mechanic.getGarage().getId().equals(garage.getId())) {
            throw new ApiException(
                    ErrorCode.UNAUTHORIZED,
                    "Mechanic does not belong to this garage"
            );
        }

        try {
            mechanic.setStatus(MechanicStatus.valueOf(request.getStatus().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Invalid mechanic status"
            );
        }

        mechanicRepository.save(mechanic); // Persist status change

        return MechanicMapper.toResponse(mechanic);
    }

    // Delete a mechanic permanently
    @Override
    @Transactional
    public void deleteMechanic(String mechanicId) {

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"
                ));

        UserAccount userAccount = mechanic.getUserAccount();

        mechanicRepository.delete(mechanic);

        if (userAccount != null) {
            userAccountRepository.delete(userAccount);
        }
    }
}
