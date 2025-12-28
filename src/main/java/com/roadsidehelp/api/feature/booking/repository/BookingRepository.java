package com.roadsidehelp.api.feature.booking.repository;

import com.roadsidehelp.api.feature.booking.entity.Booking;
import com.roadsidehelp.api.feature.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Booking> findByIdAndUserId(String bookingId, String userId);

    List<Booking> findByGarageId(String garageId);

    List<Booking> findByMechanicId(String mechanicId);

    @Query("SELECT b FROM Booking b WHERE b.mechanic.id = :mechanicId AND b.status IN :status")
    List<Booking> findByMechanicIdAndStatusIn(@Param("mechanicId") String mechanicId, @Param("status") List<BookingStatus> status);

    @Query("SELECT b FROM Booking b WHERE b.mechanic.id = :mechanicId AND b.status = :status")
    List<Booking> findByMechanicIdAndStatus(@Param("mechanicId") String mechanicId, @Param("status") BookingStatus status);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByGarageIdOrderByCreatedAtDesc(String garageId);

    List<Booking> findAllByOrderByCreatedAtDesc();

}
