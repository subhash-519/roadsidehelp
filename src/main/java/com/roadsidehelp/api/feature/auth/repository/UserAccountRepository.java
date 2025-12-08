package com.roadsidehelp.api.feature.auth.repository;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserAccount> findByVerificationToken(String token);
    Optional<UserAccount> findByEmailOrPhoneNumber(String email, String phone);
    Optional<UserAccount> findByResetPasswordToken(String token);

}
