package com.roadsidehelp.api.feature.auth.repository;

import com.roadsidehelp.api.feature.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(String userId);

    List<RefreshToken> findAllByUserId(String userId);

}
