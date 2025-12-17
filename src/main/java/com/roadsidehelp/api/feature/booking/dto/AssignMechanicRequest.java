package com.roadsidehelp.api.feature.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignMechanicRequest {

    @NotNull
    private String bookingId;

    @NotNull
    private String mechanicId;
}
