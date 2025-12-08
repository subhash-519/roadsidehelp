package com.roadsidehelp.api.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAddressRequest {

    @NotBlank
    @Size(max = 120)
    private String addressLine1;

    @Size(max = 120)
    private String addressLine2;

    @NotBlank
    @Size(max = 80)
    private String city;

    @NotBlank
    @Size(max = 80)
    private String state;

    @NotBlank
    @Size(max = 80)
    private String country;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Invalid pincode")
    private String postalCode;

}
