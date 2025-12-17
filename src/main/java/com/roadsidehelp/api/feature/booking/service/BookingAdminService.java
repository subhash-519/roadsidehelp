package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.feature.booking.dto.BookingResponse;

import java.util.List;

public interface BookingAdminService {

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(String bookingId);

    BookingResponse forceUpdateStatus(String bookingId, String status);

    void deleteBooking(String bookingId);
}
