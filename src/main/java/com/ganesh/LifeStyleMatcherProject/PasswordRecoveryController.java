package com.ganesh.LifeStyleMatcherProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class PasswordRecoveryController {

    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private AESUtil aesUtil;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<User> user = userRepository.findByEmail(email);

        Map<String, String> response = new HashMap<>();
        if (user.isEmpty()) {
            response.put("message", "Email not registered");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String otp = String.valueOf((int)(Math.random() * 900000 + 100000)); // 6-digit OTP
        otpService.saveOtp(email, otp);

        emailService.sendOtpEmail(email, otp);

        response.put("message", "OTP sent to your email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        Map<String, String> response = new HashMap<>();
        if (otpService.verifyOtp(email, otp)) {
            otpService.clearOtp(email);
            response.put("message", "OTP Verified");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid OTP");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        response.put("message", "Password reset successful!");
        return ResponseEntity.ok(response);
    }
}

