package com.roadsidehelp.api.feature.garage.dto;

import com.roadsidehelp.api.feature.garage.entity.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GarageOwnerStatusResponse {

    private boolean hasGarage;
    private KycStatus kycStatus;
    private boolean verified;
    private String message;
}
