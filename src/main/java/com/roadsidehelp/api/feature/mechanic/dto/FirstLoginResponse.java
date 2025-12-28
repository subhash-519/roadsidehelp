package com.roadsidehelp.api.feature.mechanic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirstLoginResponse {
    private String message;
    private String mechanicId;
}
