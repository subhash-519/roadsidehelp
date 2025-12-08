package com.roadsidehelp.api.feature.user.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UpdateProfileRequest {

    @Size(max = 30)
    private String gender;

    @Past
    private LocalDate dateOfBirth;

    @Size(max = 500)
    private String profileImageUrl;

}
