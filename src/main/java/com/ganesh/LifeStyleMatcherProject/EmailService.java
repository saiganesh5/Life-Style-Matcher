package com.ganesh.LifeStyleMatcherProject;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public void sendOtpEmail(String to, String otp) {
        String subject = "🔐 OTP Verification - LifeStyle Matcher";
        String body = "<h3>Hello,</h3> "+
                "<p>Your OTP for resetting the password is:</p>" +
                "<h2 style='color:#2e6da4;'>" + otp + "</h2>" +
                "<p>This OTP is valid for 5 minutes.</p>" +
                "<p>Please do not share this OTP with anyone.</p>" +
                "<p>If you did not request for this OTP, please ignore this email.</p>" +
                "<br><p>Regards,<br>LifeStyle Matcher Team</p>";

        mailSender(to, subject, body);
    }

    public void sendSignupOtpEmail(String to, String otp) {
        String subject = "🔐 OTP Verification - LifeStyle Matcher";
        String body = "<h3>Hello,</h3> "+
                "<p>Your OTP for signing up to LifeStyleMatcher is:</p>" +
                "<h2 style='color:#2e6da4;'>" + otp + "</h2>" +
                "<p>This OTP is valid for 5 minutes.</p>" +
                "<p>Please do not share this OTP with anyone.</p>" +
                "<p>If you did not request for this OTP, please ignore this email.</p>" +
                "<br><p>Regards,<br>LifeStyle Matcher Team</p>";

        mailSender(to, subject, body);

    }

    private void mailSender(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String fromAddress = env.getProperty("spring.mail.from", env.getProperty("spring.mail.username"));
            if (fromAddress == null || fromAddress.isBlank()) {
                throw new IllegalStateException("Mail sender 'from' address is not configured.");
            }
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            mailSender.send(message);
            System.out.println("OTP Email sent to " + to);

        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
}
