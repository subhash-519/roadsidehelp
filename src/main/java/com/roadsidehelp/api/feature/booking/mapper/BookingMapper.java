package com.roadsidehelp.api.feature.booking.mapper;

import com.roadsidehelp.api.feature.booking.dto.*;
import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;

public class BookingMapper {

    private BookingMapper() {
        // private constructor to prevent instantiation
    }

    /**
     * Convert Booking entity to BookingResponse DTO
     */
    public static BookingResponse toResponse(Booking booking) {
        if (booking == null) return null;

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .userId(booking.getUser() != null ? booking.getUser().getId() : null)
                .garageId(booking.getGarage() != null ? booking.getGarage().getId() : null)
                .vehicleId(booking.getVehicle() != null ? booking.getVehicle().getId() : null)
                .mechanicId(booking.getMechanic() != null ? booking.getMechanic().getId() : null)
                .bookingType(booking.getBookingType())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus())
                .estimatedPrice(booking.getEstimatedPrice())
                .problemDescription(booking.getProblemDescription())
                .pickupLatitude(booking.getPickupLatitude())
                .pickupLongitude(booking.getPickupLongitude())
                .pickupAddress(booking.getPickupAddress())
                .scheduledAt(booking.getScheduledAt())
                .acceptedAt(booking.getAcceptedAt())
                .startedAt(booking.getStartedAt())
                .completedAt(booking.getCompletedAt())
                .build();
    }

    /**
     * Convert CreateBookingRequest DTO to Booking entity
     * Useful when creating a new booking
     */
    public static Booking toEntity(CreateBookingRequest request) {
        if (request == null) return null;

        return Booking.builder()
                .bookingType(request.getBookingType())
                .problemDescription(request.getProblemDescription())
                .pickupLatitude(request.getPickupLatitude())
                .pickupLongitude(request.getPickupLongitude())
                .pickupAddress(request.getPickupAddress())
                .scheduledAt(request.getScheduledAt())
                .status(null)          // set in service layer, e.g., PENDING
                .paymentStatus(null)   // set in service layer, e.g., PENDING
                .build();
    }

    /**
     * Update Booking entity status using UpdateBookingStatusRequest DTO
     */
    public static void updateStatus(Booking booking, UpdateBookingStatusRequest request) {
        if (booking == null || request == null) return;

        booking.setStatus(request.getStatus());

        // Set cancellation or rejection reason if provided
        if (request.getReason() != null && !request.getReason().isBlank()) {
            switch (request.getStatus()) {
                case CANCELLED:
                    booking.setCancellationReason(request.getReason());
                    break;
                case REJECTED:
                    booking.setRejectionReason(request.getReason());
                    break;
                default:
                    // do nothing
            }
        }
    }

    /**
     * Assign mechanic to booking (used with AssignMechanicRequest DTO)
     */
    public static void assignMechanic(Booking booking, Mechanic mechanic) {
        if (booking == null || mechanic == null) return;
        booking.setMechanic(mechanic);
    }

}
