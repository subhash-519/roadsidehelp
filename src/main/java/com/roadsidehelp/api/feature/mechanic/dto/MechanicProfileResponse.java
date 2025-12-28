package com.roadsidehelp.api.feature.mechanic.dto;

import com.roadsidehelp.api.feature.auth.entity.UserRole;
import com.roadsidehelp.api.feature.mechanic.entity.MechanicStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class MechanicProfileResponse {

    // Mechanic domain
    private String mechanicId;
    private String fullName;
    private MechanicStatus status;
    private boolean available;

    // Garage
    private String garageId;

    // Auth (derived from UserAccount)
    private String userAccountId;
    private String email;
    private String phoneNumber;
    private Set<UserRole> roles;
}

