package com.roadsidehelp.api.feature.auth.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
