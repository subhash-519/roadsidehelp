package com.roadsidehelp.api.feature.booking.dto;

import com.roadsidehelp.api.feature.booking.entity.BookingType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequest {

    @NotNull
    private String garageId;

    @NotNull
    private String vehicleId;

    @NotNull
    private BookingType bookingType;

    @NotBlank
    @Size(max = 500)
    private String problemDescription;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double pickupLatitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double pickupLongitude;

    @Size(max = 255)
    private String pickupAddress;

    @NotNull
    @FutureOrPresent
    private OffsetDateTime scheduledAt;
}
