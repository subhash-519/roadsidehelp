package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.feature.booking.dto.AssignMechanicRequest;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.UpdateBookingStatusRequest;

import java.util.List;

public interface BookingGarageService {

    // Get all bookings of the logged-in garage
    List<BookingResponse> getGarageBookings();

    // Accept or reject a booking
    BookingResponse updateBookingStatus(String bookingId, UpdateBookingStatusRequest request);

    // Assign a mechanic to a booking
    BookingResponse assignMechanic(AssignMechanicRequest request);

    // Start the service for a booking
    BookingResponse startService(String bookingId);

    // Complete the service for a booking
    BookingResponse completeService(String bookingId);
}
