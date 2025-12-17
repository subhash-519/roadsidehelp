package com.roadsidehelp.api.feature.mechanic.repository;

import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, String> {
    // findById is already provided by JpaRepository

    List<Mechanic> findByGarageId(String garageId);
}
