package com.ganesh.LifeStyleMatcherProject;

import com.ganesh.LifeStyleMatcherProject.GmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MailController {

    @GetMapping("/sendMail")
    public String sendMail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        try {
            GmailService.sendMail(to, subject, body);
            return "✅ Email sent to " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed: " + e.getMessage();
        }
    }
}
