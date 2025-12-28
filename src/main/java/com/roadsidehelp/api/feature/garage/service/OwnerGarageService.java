package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.feature.garage.dto.CreateGarageRequest;
import com.roadsidehelp.api.feature.garage.dto.GarageDocumentRequest;
import com.roadsidehelp.api.feature.garage.dto.GarageResponse;
import com.roadsidehelp.api.feature.garage.dto.UpdateGarageRequest;

public interface OwnerGarageService {

    GarageResponse createGarage(String ownerId, CreateGarageRequest req);

    GarageResponse getMyGarage(String ownerId);

    GarageResponse updateGarage(String ownerId, UpdateGarageRequest req);

    GarageResponse updateDocuments(String ownerId, GarageDocumentRequest req);

    GarageResponse updateOpenStatus(String ownerId, boolean open);
}
