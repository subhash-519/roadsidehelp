package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.booking.dto.AssignMechanicRequest;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.dto.UpdateBookingStatusRequest;
import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import com.roadsidehelp.api.feature.booking.mapper.BookingMapper;
import com.roadsidehelp.api.feature.booking.repository.BookingRepository;
import com.roadsidehelp.api.feature.booking.validator.BookingValidator;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.repository.GarageRepository;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingGarageServiceImpl implements BookingGarageService {

    private final BookingRepository bookingRepository;
    private final GarageRepository garageRepository;
    private final MechanicRepository mechanicRepository;
    private final BookingValidator bookingValidator;

    /* =========================
       GET GARAGE BOOKINGS
       ========================= */
    @Transactional(readOnly = true)
    public List<BookingResponse> getGarageBookings() {

        String ownerId = CurrentUser.getUserId();

        if (ownerId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        Garage garage = garageRepository.findByOwnerId(ownerId);

        if (garage == null) {
            throw new ApiException(ErrorCode.GARAGE_NOT_FOUND, "Garage not found");
        }

        return bookingRepository.findByGarageIdOrderByCreatedAtDesc(garage.getId())
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    /* =========================
       ACCEPT / REJECT BOOKING
       ========================= */
    public BookingResponse updateBookingStatus(
            String bookingId,
            UpdateBookingStatusRequest request) {

        Booking booking = getGarageBooking(bookingId);

        bookingValidator.validateGarageAction(booking);
        bookingValidator.validateStatusTransition(
                booking.getStatus(),
                request.getStatus()
        );

        if (request.getStatus() == BookingStatus.ACCEPTED) {
            booking.setStatus(BookingStatus.ACCEPTED);
            booking.setAcceptedAt(OffsetDateTime.now());
        }

        if (request.getStatus() == BookingStatus.REJECTED) {
            booking.setStatus(BookingStatus.REJECTED);
            booking.setRejectionReason(request.getReason());
        }

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       ASSIGN MECHANIC
       ========================= */
    public BookingResponse assignMechanic(AssignMechanicRequest request) {

        Booking booking = getGarageBooking(request.getBookingId());

        bookingValidator.validateMechanicAssignment(booking);

        Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
                .orElseThrow(() -> new ApiException(
                        ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));

        // Mechanic must belong to this garage
        if (!mechanic.getGarage().getId().equals(booking.getGarage().getId())) {
            throw new ApiException(
                    ErrorCode.MECHANIC_NOT_IN_GARAGE,
                    "Mechanic does not belong to this garage"
            );
        }

        booking.setMechanic(mechanic);

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       START SERVICE
       ========================= */
    public BookingResponse startService(String bookingId) {

        Booking booking = getGarageBooking(bookingId);

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Service can start only after booking is ACCEPTED"
            );
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking.setStartedAt(OffsetDateTime.now());

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       COMPLETE SERVICE
       ========================= */
    public BookingResponse completeService(String bookingId) {

        Booking booking = getGarageBooking(bookingId);

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Service can be completed only if IN_PROGRESS"
            );
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(OffsetDateTime.now());

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       INTERNAL
       ========================= */
    private Booking getGarageBooking(String bookingId) {

        String ownerId = CurrentUser.getUserId();

        Garage garage = garageRepository.findByOwnerId(ownerId);

        if (garage == null) {
            throw new ApiException(ErrorCode.GARAGE_NOT_FOUND, "Garage not found");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND, "Booking not found"));

        if (!booking.getGarage().getId().equals(garage.getId())) {
            throw new ApiException(
                    ErrorCode.UNAUTHORIZED,
                    "Booking does not belong to your garage"
            );
        }

        return booking;
    }
}
