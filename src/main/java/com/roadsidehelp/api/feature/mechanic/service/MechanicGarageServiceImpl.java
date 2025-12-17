package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicGarageServiceImpl implements MechanicGarageService {

    private final MechanicRepository mechanicRepository;
    private final GarageRepository garageRepository;

    // Create a new mechanic for a garage
    @Override
    public MechanicResponse createMechanic(String garageId, CreateMechanicRequest request) {

        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found"));

        Mechanic mechanic = Mechanic.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .status(MechanicStatus.ACTIVE)
                .garage(garage)
                .available(true)
                .build();

        mechanicRepository.save(mechanic);

        return MechanicMapper.toResponse(mechanic);
    }

    // Get all mechanics belonging to a garage
    @Override
    public List<MechanicResponse> getGarageMechanics(String garageId) {

        return mechanicRepository.findByGarageId(garageId)
                .stream()
                .map(MechanicMapper::toResponse)
                .toList();
    }

    @Override
    public MechanicResponse updateMechanicStatus(String garageId, String mechanicId, UpdateMechanicStatusRequest request) {

        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found"));

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));

        if (!mechanic.getGarage().getId().equals(garage.getId())) {
            throw new ApiException(
                    ErrorCode.UNAUTHORIZED,
                    "Mechanic does not belong to this garage"
            );
        }

        // Update status
        try {
            MechanicStatus status = MechanicStatus.valueOf(request.getStatus().toUpperCase());
            mechanic.setStatus(status);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Invalid mechanic status value"
            );
        }

        return MechanicMapper.toResponse(mechanic);
    }
}
