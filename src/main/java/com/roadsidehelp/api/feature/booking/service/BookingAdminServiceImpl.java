package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import com.roadsidehelp.api.feature.booking.mapper.BookingMapper;
import com.roadsidehelp.api.feature.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingAdminServiceImpl implements BookingAdminService {

    private final BookingRepository bookingRepository;

    /* =========================
       GET ALL BOOKINGS
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    /* =========================
       GET BOOKING BY ID
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found"
                ));

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       FORCE UPDATE STATUS (ADMIN)
       ========================= */
    @Override
    public BookingResponse forceUpdateStatus(String bookingId, String status) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found"
                ));

        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Invalid booking status"
            );
        }

        booking.setStatus(newStatus);

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       DELETE BOOKING
       ========================= */
    @Override
    public void deleteBooking(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found"
                ));

        bookingRepository.delete(booking);
    }
}
