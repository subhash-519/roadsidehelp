package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import com.roadsidehelp.api.config.security.jwt.JwtService;
import com.roadsidehelp.api.feature.booking.dto.BookingLocationResponse;
import com.roadsidehelp.api.feature.booking.dto.BookingResponse;
import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import com.roadsidehelp.api.feature.booking.mapper.BookingMapper;
import com.roadsidehelp.api.feature.booking.repository.BookingRepository;
import com.roadsidehelp.api.feature.booking.validator.BookingValidator;
import com.roadsidehelp.api.feature.mechanic.dto.*;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import com.roadsidehelp.api.feature.mechanic.mapper.MechanicMapper;
import com.roadsidehelp.api.feature.mechanic.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepo;
    private final BookingRepository bookingRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BookingValidator bookingValidator;
    private final MechanicRepository mechanicRepository;

    // ===================== AUTH (NO JWT) =====================
    @Override
    public String firstLogin(MechanicFirstLoginRequest request) {
        Mechanic mechanic = mechanicRepo.findByEmailOrPhone(request.getUsername())
                .orElseThrow(() -> new ApiException(ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));

        if (!mechanic.isFirstLogin()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Mechanic has already completed first login");
        }

        if (!passwordEncoder.matches(request.getTempPassword(), mechanic.getUserAccount().getPasswordHash())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid temporary password");
        }

        if (!mechanic.getUserAccount().isVerified()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Please verify your email first");
        }

        return mechanic.getUserAccount().getId(); // return UserAccount ID for JWT
    }

    @Override
    public void setPassword(String userAccountId, MechanicSetPasswordRequest request) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);

        if (!mechanic.isFirstLogin()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Password already set for this mechanic");
        }

        mechanic.getUserAccount().setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        mechanic.setFirstLogin(false);
    }

    @Override
    public String login(MechanicLoginRequest request) {
        Mechanic mechanic = mechanicRepo.findByEmailOrPhone(request.getUsername())
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials"));

        if (mechanic.isFirstLogin()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Complete first login by setting password");
        }

        if (!passwordEncoder.matches(request.getPassword(), mechanic.getUserAccount().getPasswordHash())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        // Use UserAccount ID in JWT
        return jwtService.generateAccessToken(
                mechanic.getUserAccount().getId(),
                mechanic.getUserAccount().getFullName(),
                mechanic.getUserAccount().getRoles()
        );
    }

    // ===================== PROFILE (JWT REQUIRED) =====================
    @Override
    public MechanicResponse getMyProfile(String userAccountId) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);
        return MechanicMapper.toResponse(mechanic);
    }

    // ===================== BOOKINGS (JWT REQUIRED) =====================
    @Override
    public List<BookingResponse> getAssignedBookings(String userAccountId) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);
        return bookingRepo.findByMechanicIdAndStatusIn(
                        mechanic.getId(),
                        List.of(BookingStatus.MECHANIC_ASSIGNED, BookingStatus.IN_PROGRESS)
                )
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getCompletedBookings(String userAccountId) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);
        return bookingRepo.findByMechanicIdAndStatus(
                        mechanic.getId(),
                        BookingStatus.COMPLETED
                )
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingLocationResponse getBookingLocation(String userAccountId, String bookingId) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);
        Booking booking = getAssignedBooking(mechanic.getId(), bookingId);

        return BookingLocationResponse.builder()
                .bookingId(booking.getId())
                .garageId(booking.getGarage().getId())
                .latitude(booking.getPickupLatitude())
                .longitude(booking.getPickupLongitude())
                .address(booking.getPickupAddress())
                .status(booking.getStatus().name())
                .build();
    }

    // ===================== START SERVICE =====================
    @Override
    public void startService(String userAccountId, String bookingId) {
        Mechanic mechanic = getMechanicByUserAccountId(userAccountId);
        Booking booking = getAssignedBooking(mechanic.getId(), bookingId);

        if (booking.getStatus() != BookingStatus.MECHANIC_ASSIGNED) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Service cannot be started yet");
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking.setStartedAt(OffsetDateTime.now());
    }

    // ===================== COMPLETE BOOKING =====================
    @Override
    public void completeBooking(String userAccountId, String bookingId) {

        // Get logged-in mechanic
        Mechanic loggedInMechanic = getMechanicByUserAccountId(userAccountId);

        // Get booking assigned to this mechanic
        Booking booking = getAssignedBooking(loggedInMechanic.getId(), bookingId);

        bookingValidator.validateCompletion(booking);

        // Mark booking as completed
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(OffsetDateTime.now());

        // Free up mechanic
        Mechanic assignedMechanic = booking.getMechanic();
        if (assignedMechanic != null) {
            assignedMechanic.setAvailable(true);
            mechanicRepository.save(assignedMechanic);
        }

        bookingRepo.save(booking);
    }

    // ===================== HELPERS =====================
    private Booking getAssignedBooking(String mechanicId, String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND, "Booking not found"));

        if (booking.getMechanic() == null || !booking.getMechanic().getId().equals(mechanicId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "Not assigned to this booking");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Booking already closed");
        }

        return booking;
    }

    private Mechanic getMechanicByUserAccountId(String userAccountId) {
        return mechanicRepo.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new ApiException(ErrorCode.MECHANIC_NOT_FOUND, "Mechanic not found"));
    }
}
