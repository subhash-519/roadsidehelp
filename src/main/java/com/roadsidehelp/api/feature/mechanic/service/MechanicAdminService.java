package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;

import java.util.List;

public interface MechanicAdminService {

    // Get all mechanics
    List<MechanicResponse> getAllMechanics();

    // Deactivate a mechanic
    MechanicResponse deactivateMechanic(String mechanicId);

    // Activate a mechanic
    MechanicResponse activateMechanic(String mechanicId);

    // Delete a mechanic permanently
    void deleteMechanic(String mechanicId);
}
