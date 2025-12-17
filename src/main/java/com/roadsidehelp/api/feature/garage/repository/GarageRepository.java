package com.roadsidehelp.api.feature.garage.repository;

import com.roadsidehelp.api.feature.garage.entity.Garage;
import com.roadsidehelp.api.feature.garage.entity.GarageStatus;
import com.roadsidehelp.api.feature.garage.entity.GarageType;
import com.roadsidehelp.api.feature.garage.entity.KycStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, String> {

    // Owner's garage
    Garage findByOwnerId(String ownerId);

    // Owner's garage (used by BookingGarageService)
    Optional<Garage> findByOwner_Id(String ownerId);

    // Public: list all (with optional filters handled in service)
    List<Garage> findByCityIgnoreCase(String city);

    // Admin: pending verification
    List<Garage> findByVerifiedFalse();

    // Admin: all verified garages
    List<Garage> findByVerifiedTrue();

    List<Garage> findByKycStatus(KycStatus kycStatus);

    // 🔹 Public use-case
    List<Garage> findByKycStatusAndGarageStatus(
            KycStatus kycStatus,
            GarageStatus garageStatus
    );

    // All public garages
    @Query("""
        SELECT g FROM Garage g
        WHERE g.kycStatus = :kycStatus
          AND g.verified = true
    """)
    List<Garage> findPublicGarages(KycStatus kycStatus);

    // Public garage by ID
    @Query("""
        SELECT g FROM Garage g
        WHERE g.id = :garageId
          AND g.kycStatus = :kycStatus
          AND g.verified = true
    """)
    Optional<Garage> findPublicGarageById(
            String garageId,
            KycStatus kycStatus
    );

    // Nearby public garages (Haversine)
    @Query("""
    SELECT g FROM Garage g
    WHERE g.kycStatus = :kycStatus
      AND g.verified = true
      AND g.latitude IS NOT NULL
      AND g.longitude IS NOT NULL
      AND (
        6371 * acos(
            cos(radians(:lat)) *
            cos(radians(g.latitude)) *
            cos(radians(g.longitude) - radians(:lng)) +
            sin(radians(:lat)) *
            sin(radians(g.latitude))
        )
      ) <= :radiusKm
    ORDER BY (
        6371 * acos(
            cos(radians(:lat)) *
            cos(radians(g.latitude)) *
            cos(radians(g.longitude) - radians(:lng)) +
            sin(radians(:lat)) *
            sin(radians(g.latitude))
        )
    )
""")
    List<Garage> findNearbyPublicGarages(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusKm") double radiusKm,
            @Param("kycStatus") KycStatus kycStatus
    );

    // Search public garages
    @Query("""
        SELECT g FROM Garage g
        WHERE g.kycStatus = :kycStatus
          AND g.verified = true
          AND (:city IS NULL OR LOWER(g.city) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:type IS NULL OR g.garageType = :type)
          AND (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    List<Garage> searchPublicGarages(
            String city,
            GarageType type,
            String name,
            KycStatus kycStatus
    );
}
