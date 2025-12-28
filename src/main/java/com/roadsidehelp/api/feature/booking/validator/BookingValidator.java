package com.roadsidehelp.api.feature.booking.validator;

import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import com.roadsidehelp.api.feature.booking.entity.PaymentStatus;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.vehicle.entity.Vehicle;
import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class BookingValidator {

    // ===================== CREATE BOOKING =====================
    public void validateCreateBooking(Garage garage, Vehicle vehicle, String userId) {
        if (garage == null) throw new ApiException(ErrorCode.GARAGE_NOT_FOUND, "Garage not found");
        if (vehicle == null) throw new ApiException(ErrorCode.VEHICLE_NOT_FOUND, "Vehicle not found");
        if (!garage.isApproved()) throw new ApiException(ErrorCode.GARAGE_NOT_VERIFIED, "Garage is not approved");
        if (!garage.isOpen()) throw new ApiException(ErrorCode.GARAGE_CLOSED, "Garage is currently closed");
        if (!vehicle.getUser().getId().equals(userId)) throw new ApiException(ErrorCode.VEHICLE_NOT_OWNED, "Vehicle does not belong to the user");
    }

    // ===================== GARAGE ACTION (ACCEPT / REJECT) =====================
    public void validateGarageAction(Booking booking) {
        validateBookingExists(booking);
        if (booking.getStatus() != BookingStatus.REQUESTED) {
            throw new ApiException(ErrorCode.BOOKING_NOT_ACCEPT, "Only REQUESTED bookings can be accepted or rejected");
        }
    }

    // ===================== MECHANIC ASSIGNMENT =====================
    public void validateMechanicAssignment(Booking booking) {
        validateBookingExists(booking);
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new ApiException(ErrorCode.BOOKING_NOT_ACCEPT, "Mechanic can only be assigned after booking is ACCEPTED");
        }
        if (booking.getMechanic() != null) {
            throw new ApiException(ErrorCode.MECHANIC_ALREADY_ASSIGNED, "Mechanic already assigned to this booking");
        }
    }

    // ===================== USER CANCELLATION =====================
    public void validateUserCancellation(Booking booking) {
        validateBookingExists(booking);
        if (booking.getStatus() == BookingStatus.IN_PROGRESS || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new ApiException(ErrorCode.BOOKING_NOT_CANCELED, "Booking cannot be canceled after service has started");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ApiException(ErrorCode.BOOKING_NOT_CANCELED, "Booking is already canceled");
        }
    }

    // ===================== BOOKING COMPLETION =====================
    public void validateCompletion(Booking booking) {
        validateBookingExists(booking);

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new ApiException(
                    ErrorCode.INVALID_REQUEST,
                    "Booking must be IN_PROGRESS to complete service"
            );
        }

        if (booking.getPaymentStatus() != PaymentStatus.PAID) {
            throw new ApiException(
                    ErrorCode.PAYMENT_PENDING,
                    "Payment required before completing service"
            );
        }
    }

    // ===================== STATUS TRANSITION SAFETY =====================
    public void validateStatusTransition(BookingStatus current, BookingStatus next) {
        if (current == BookingStatus.COMPLETED) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Completed booking cannot change state");
        }
        if (current == BookingStatus.REQUESTED && next == BookingStatus.IN_PROGRESS) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Booking must be ACCEPTED before starting");
        }
    }

    // ===================== COMMON VALIDATION =====================
    private void validateBookingExists(Booking booking) {
        if (booking == null) throw new ApiException(ErrorCode.BOOKING_NOT_FOUND, "Booking not found");
    }
}
