package com.roadsidehelp.api.feature.mechanic.mapper;

import com.roadsidehelp.api.feature.mechanic.dto.CreateMechanicRequest;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.entity.MechanicStatus;

public class MechanicMapper {

    private MechanicMapper() {
        // private constructor to prevent instantiation
    }

    // Convert Mechanic entity to MechanicResponse DTO
    public static MechanicResponse toResponse(Mechanic mechanic) {
        if (mechanic == null) return null;

        return MechanicResponse.builder()
                .id(mechanic.getId())
                .fullName(mechanic.getFullName())
                .phone(mechanic.getPhone())
                .status(mechanic.getStatus().name())
                .garageId(mechanic.getGarage() != null ? mechanic.getGarage().getId() : null)
                .garageName(mechanic.getGarage() != null ? mechanic.getGarage().getName() : null)
                .build();
    }

    // Convert CreateMechanicRequest DTO to Mechanic entity
    public static Mechanic toEntity(CreateMechanicRequest request) {
        if (request == null) return null;

        return Mechanic.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .available(true) // default available
                .build();
    }

    // Update Mechanic status
    public static void updateStatus(Mechanic mechanic, String status) {
        if (mechanic == null || status == null || status.isBlank()) return;

        mechanic.setStatus(Enum.valueOf(MechanicStatus.class, status.toUpperCase()));
    }
}
