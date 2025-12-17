package com.roadsidehelp.api.feature.booking.repository;

import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    // Get all bookings of a user ordered by latest first
    List<Booking> findByUserIdOrderByCreatedAtDesc(String userId);

    // Get a specific booking by bookingId and userId
    Optional<Booking> findByIdAndUserId(String bookingId, String userId);

    // Get all bookings of a garage
    List<Booking> findByGarageId(String garageId);

    // Get all bookings assigned to a mechanic
    List<Booking> findByMechanicId(String mechanicId);

    // Get bookings by status (admin / dashboard)
    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByGarageIdOrderByCreatedAtDesc(String garageId);

    List<Booking> findAllByOrderByCreatedAtDesc();

}
