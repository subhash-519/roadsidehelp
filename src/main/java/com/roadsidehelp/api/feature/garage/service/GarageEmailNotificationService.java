package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;

public interface GarageEmailNotificationService {
    void sendApprovalNotification(UserAccount userAccount);
}
