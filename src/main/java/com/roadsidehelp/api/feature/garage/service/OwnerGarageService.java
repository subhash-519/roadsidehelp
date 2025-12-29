package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.feature.garage.dto.*;

public interface OwnerGarageService {

    GarageResponse createGarage(String ownerId, CreateGarageRequest req);

    GarageResponse getMyGarage(String ownerId);

    GarageResponse updateGarage(String ownerId, UpdateGarageRequest req);

    GarageResponse updateDocuments(String ownerId, GarageDocumentRequest req);

    GarageResponse updateOpenStatus(String ownerId, boolean open);

    GarageOwnerStatusResponse getMyGarageStatus(String ownerId);
}
