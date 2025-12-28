package com.roadsidehelp.api.feature.mechanic.repository;

import com.roadsidehelp.api.feature.mechanic.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, String> {

    Optional<Mechanic> findByUserAccountPhoneNumber(String phone);

    @Query("SELECT m FROM Mechanic m WHERE m.userAccount.email = :username OR m.userAccount.phoneNumber = :username")
    Optional<Mechanic> findByEmailOrPhone(@Param("username") String username);

    Optional<Mechanic> findByUserAccountVerificationToken(String verificationToken);

    boolean existsByUserAccountEmail(String email);

    boolean existsByUserAccountPhoneNumber(String phone);

    List<Mechanic> findByGarageId(String garageId);

    Optional<Mechanic> findByUserAccountId(String userAccountId);
}
