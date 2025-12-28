package com.roadsidehelp.api.feature.mechanic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MechanicFirstLoginRequest {

    @NotBlank(message = "Email or phone number is required")
    private String username;

    @NotBlank(message = "Temporary password is required")
    private String tempPassword;
}
