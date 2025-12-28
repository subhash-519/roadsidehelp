package com.roadsidehelp.api.feature.garage.service;

import com.roadsidehelp.api.feature.auth.entity.UserAccount;
import com.roadsidehelp.api.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GarageEmailNotificationServiceImpl implements GarageEmailNotificationService {

    private final EmailService emailService;

    @Override
    public void sendApprovalNotification(UserAccount userAccount) {

        String body = """
        Hello %s,

        We are pleased to inform you that your garage has been successfully reviewed and approved by our administrative team.

        You can now log in to your account and gain full access to all garage features available on the Roadside Help platform. This includes managing your garage profile, receiving and responding to service requests, tracking jobs, and engaging with customers who need your expertise.

        We encourage you to complete your garage profile with accurate information such as services offered, operating hours, and contact details to help customers find and trust your garage more easily.

        If you experience any issues while accessing your account or have questions about using the platform, our support team is always ready to assist you.

        Thank you for joining Roadside Help and for being part of our trusted service provider network. We look forward to working with you and supporting your success on our platform.

        Warm regards,
        Roadside Help Team
        """.formatted(userAccount.getFullName());

        emailService.send(
                userAccount.getEmail(),
                "Your Garage Has Been Approved",
                body
        );
    }
}
