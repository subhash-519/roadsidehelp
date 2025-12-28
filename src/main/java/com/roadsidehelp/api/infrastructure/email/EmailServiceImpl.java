package com.roadsidehelp.api.infrastructure.email;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String subject, String message) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // ✅ NO-REPLY SETUP (WRITE HERE)
            helper.setFrom("Roadside Help (No Reply) <absubhash07@gmail.com>");
            helper.setReplyTo("no-reply@invalid.com");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, false);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new ApiException(ErrorCode.EMAIL_SEND_FAILED, e.toString());
        }
    }
}
