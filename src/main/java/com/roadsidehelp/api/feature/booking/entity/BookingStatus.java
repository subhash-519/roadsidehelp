package com.roadsidehelp.api.feature.booking.entity;

public enum BookingStatus {
    REQUESTED,           // User created
    ACCEPTED,            // Garage accepted
    MECHANIC_ASSIGNED,   // Mechanic assigned
    CONFIRMED,           // Ready for payment
    IN_PROGRESS,         // Service started
    PAID,
    COMPLETED,           // Finished
    CANCELLED,            // User / Garage / Admin
    REJECTED,
}

