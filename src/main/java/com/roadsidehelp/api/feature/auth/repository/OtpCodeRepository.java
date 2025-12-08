package com.roadsidehelp.api.feature.auth.repository;

import com.roadsidehelp.api.feature.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, String> {

    void deleteByUserId(String userId);

    Optional<OtpCode> findTopByUserIdAndUsedFalseOrderByExpiresAtDesc(String userId);
}
