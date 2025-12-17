package com.roadsidehelp.api.feature.booking.entity;

import com.roadsidehelp.api.core.domain.BaseEntity;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.vehicle.entity.Vehicle;
import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "booking",
        indexes = {
                @Index(name = "idx_booking_user", columnList = "user_id"),
                @Index(name = "idx_booking_garage", columnList = "garage_id"),
                @Index(name = "idx_booking_status", columnList = "status"),
                @Index(name = "idx_booking_mechanic", columnList = "mechanic_id")
        }
)
public class Booking extends BaseEntity {

    // User who created the booking
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserAccount user;

    // Garage handling the booking
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "garage_id", nullable = false)
    @NotNull
    private Garage garage;

    // Vehicle for which service is requested
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @NotNull
    private Vehicle vehicle;

    // Mechanic assigned after acceptance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;

    // Type of booking (ROAD_SIDE, GARAGE, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull
    private BookingType bookingType;

    // Current booking status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull
    private BookingStatus status;

    // Payment status of the booking
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull
    private PaymentStatus paymentStatus;

    // User described problem
    @Column(length = 500)
    @Size(max = 500)
    private String problemDescription;

    // Estimated service cost
    @NotNull
    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @NotNull
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double pickupLatitude;

    @NotNull
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double pickupLongitude;

    // Human-readable pickup address
    @Column(length = 255)
    private String pickupAddress;

    // Time scheduled by the user
    @NotNull
    @FutureOrPresent
    private OffsetDateTime scheduledAt;

    // Time when booking was accepted
    private OffsetDateTime acceptedAt;

    // Time when service started
    private OffsetDateTime startedAt;

    // Time when service completed
    private OffsetDateTime completedAt;

    // Reason for garage rejection
    @Column(length = 255)
    private String rejectionReason;

    // Reason for user cancellation
    @Column(length = 255)
    private String cancellationReason;
}
