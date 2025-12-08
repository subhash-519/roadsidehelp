package com.roadsidehelp.api.feature.user.repository;

import com.roadsidehelp.api.feature.user.entity.UserProfile;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUser(UserAccount user);
}
