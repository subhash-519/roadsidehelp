package com.roadsidehelp.api.feature.mechanic.service;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;

public interface MechanicEmailVerificationService {
    public void sendVerification(UserAccount userAccount, String tempPassword);
    void verifyEmail(String token);
}

