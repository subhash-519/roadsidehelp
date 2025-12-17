package com.roadsidehelp.api.feature.mechanic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMechanicRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phone;
}
