package com.roadsidehelp.api.feature.booking.dto;

import com.roadsidehelp.api.feature.booking.entity.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private String bookingId;
    private String userId;
    private String garageId;
    private String vehicleId;
    private String mechanicId;
    private BookingType bookingType;
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private BigDecimal estimatedPrice;
    private String problemDescription;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String pickupAddress;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime acceptedAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
}

