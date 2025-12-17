package com.roadsidehelp.api.feature.booking.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.core.utils.CurrentUser;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.auth.repository.UserAccountRepository;
import com.roadsidehelp.api.feature.booking.dto.CreateBookingRequest;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import com.roadsidehelp.api.feature.booking.entity.PaymentStatus;
import com.roadsidehelp.api.feature.booking.mapper.BookingMapper;
import com.roadsidehelp.api.feature.booking.repository.BookingRepository;
import com.roadsidehelp.api.feature.booking.validator.BookingValidator;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.repository.GarageRepository;
import com.roadsidehelp.api.feature.vehicle.entity.Vehicle;
import com.roadsidehelp.api.feature.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingUserServiceImpl implements BookingUserService {

    private final BookingRepository bookingRepository;
    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;
    private final UserAccountRepository userRepository;
    private final BookingValidator bookingValidator;

    /* =========================
       CREATE BOOKING
       ========================= */
    public BookingResponse createBooking(CreateBookingRequest request) {

        String userId = CurrentUser.getUserId();

        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND, "User not found"));

        Garage garage = garageRepository.findById(request.getGarageId())
                .orElseThrow(() -> new ApiException(
                        ErrorCode.GARAGE_NOT_FOUND, "Garage not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ApiException(
                        ErrorCode.VEHICLE_NOT_FOUND, "Vehicle not found"));

        // Validation
        bookingValidator.validateCreateBooking(garage, vehicle, userId);

        Booking booking = BookingMapper.toEntity(request);

        booking.setUser(user);
        booking.setGarage(garage);
        booking.setVehicle(vehicle);
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setEstimatedPrice(BigDecimal.ZERO); // initial estimate

        bookingRepository.save(booking);

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       GET MY BOOKINGS
       ========================= */
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {

        String userId = CurrentUser.getUserId();

        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    /* =========================
       GET BOOKING BY ID
       ========================= */
    @Transactional(readOnly = true)
    public BookingResponse getMyBookingById(String bookingId) {

        String userId = CurrentUser.getUserId();

        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND, "Booking not found"));

        return BookingMapper.toResponse(booking);
    }

    /* =========================
       CANCEL BOOKING
       ========================= */
    public BookingResponse cancelBooking(String bookingId, String reason) {

        String userId = CurrentUser.getUserId();

        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.BOOKING_NOT_FOUND, "Booking not found"));

        bookingValidator.validateUserCancellation(booking);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);

        return BookingMapper.toResponse(booking);
    }
}
