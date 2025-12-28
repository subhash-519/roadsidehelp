package com.roadsidehelp.api.feature.mechanic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MechanicLoginRequest {

    @NotBlank(message = "Email or phone number is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}

