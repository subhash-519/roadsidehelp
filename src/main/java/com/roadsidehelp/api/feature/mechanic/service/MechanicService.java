package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.BookingLocationResponse;
import com.roadsidehelp.api.feature.mechanic.dto.*;

import java.util.List;

public interface MechanicService {

    /* =====================
       AUTH
       ===================== */

    String firstLogin(MechanicFirstLoginRequest request);

    void setPassword(String mechanicId, MechanicSetPasswordRequest request);

    String login(MechanicLoginRequest request);

    /* =====================
       BOOKINGS
       ===================== */

    List<BookingResponse> getAssignedBookings(String mechanicId);

    BookingLocationResponse getBookingLocation(String mechanicId, String bookingId);

    void startService(String mechanicId, String bookingId);

    void completeBooking(String mechanicId, String bookingId);

    MechanicResponse getMyProfile(String mechanicId);

    public List<BookingResponse> getCompletedBookings(String mechanicId);

}
