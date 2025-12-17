package com.roadsidehelp.api.feature.mechanic.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MechanicResponse {

    private String id;
    private String fullName;
    private String phone;
    private String status;
    private String garageId;   // Garage reference
    private String garageName; // Optional, for display purposes
}

