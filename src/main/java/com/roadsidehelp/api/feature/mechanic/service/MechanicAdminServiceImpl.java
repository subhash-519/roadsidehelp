package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.entity.MechanicStatus;
import com.roadsidehelp.api.feature.mechanic.mapper.MechanicMapper;
import com.roadsidehelp.api.feature.mechanic.repository.MechanicRepository;
import com.roadsidehelp.api.feature.mechanic.validator.MechanicValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicAdminServiceImpl implements MechanicAdminService {

    private final MechanicRepository mechanicRepository;
    private final MechanicValidator mechanicValidator;
    private final UserAccountRepository userAccountRepository;

    // Get all mechanics
    @Override
    public List<MechanicResponse> getAllMechanics() {
        return mechanicRepository.findAll()
                .stream()
                .map(MechanicMapper::toResponse)
                .toList();
    }

    // Deactivate a mechanic
    @Override
    public MechanicResponse deactivateMechanic(String mechanicId) {
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));

        mechanic.setStatus(MechanicStatus.INACTIVE);
        return MechanicMapper.toResponse(mechanic);
    }

    // Activate a mechanic
    @Override
    public MechanicResponse activateMechanic(String mechanicId) {
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));

        mechanic.setStatus(MechanicStatus.ACTIVE);
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
