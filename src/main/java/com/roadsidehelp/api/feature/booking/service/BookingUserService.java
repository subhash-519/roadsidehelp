package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.CreateBookingRequest;

import java.util.List;

public interface BookingUserService {

    // Create a new booking by user
    BookingResponse createBooking(CreateBookingRequest request);

    // Get all bookings of logged-in user
    List<BookingResponse> getMyBookings();

    // Get a specific booking of logged-in user
    BookingResponse getMyBookingById(String bookingId);

    // Cancel a booking by user
    BookingResponse cancelBooking(String bookingId, String reason);
}
