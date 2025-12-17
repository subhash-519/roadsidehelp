package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.feature.mechanic.dto.CreateMechanicRequest;
import com.roadsidehelp.api.feature.mechanic.dto.MechanicResponse;
import com.roadsidehelp.api.feature.mechanic.dto.UpdateMechanicStatusRequest;

import java.util.List;

public interface MechanicGarageService {

    MechanicResponse createMechanic(String garageId, CreateMechanicRequest request);

    List<MechanicResponse> getGarageMechanics(String garageId);

    public MechanicResponse updateMechanicStatus(
            String garageId,
            String mechanicId,
            UpdateMechanicStatusRequest request);
}
