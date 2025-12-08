package com.roadsidehelp.api.feature.user.repository;

import com.roadsidehelp.api.feature.user.entity.UserAddress;
import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {
    Optional<UserAddress> findByUser(UserAccount user);
}
