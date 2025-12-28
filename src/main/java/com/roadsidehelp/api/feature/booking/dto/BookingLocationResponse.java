package com.roadsidehelp.api.feature.booking.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingLocationResponse {

    private String bookingId;
    private String garageId;

    private Double latitude;
    private Double longitude;
    private String address;

    private String status;
}


