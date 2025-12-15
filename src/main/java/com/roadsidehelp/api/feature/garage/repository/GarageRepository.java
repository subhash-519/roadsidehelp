package com.roadsidehelp.api.feature.garage.repository;

import com.roadsidehelp.api.feature.garage.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageRepository extends JpaRepository<Garage, String> {

    // Owner's garage
    Garage findByOwnerId(String ownerId);

    // Public: list all (with optional filters handled in service)
    List<Garage> findByCityIgnoreCase(String city);

    // Admin: pending verification
    List<Garage> findByVerifiedFalse();

    // Admin: all verified garages
    List<Garage> findByVerifiedTrue();
}
