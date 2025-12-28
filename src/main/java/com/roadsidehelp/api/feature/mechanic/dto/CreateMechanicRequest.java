package com.roadsidehelp.api.feature.mechanic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMechanicRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+91|0)?[6-9]\\d{9}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;   // matches UserAccount

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}