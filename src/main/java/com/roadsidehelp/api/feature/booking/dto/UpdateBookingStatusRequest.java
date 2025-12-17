package com.roadsidehelp.api.feature.booking.dto;

import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingStatusRequest {

    @NotNull(message = "Booking status is required")
    private BookingStatus status;

    // Optional reason field for cancellation/rejection
    private String reason;
}
