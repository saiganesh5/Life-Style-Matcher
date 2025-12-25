//package com.ganesh.LifeStyleMatcherProject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
//public class SignUpController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AESUtil aesUtil;
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody Map<String, String> request) {
//        String name = request.get("name");
//        String email = request.get("email");
//        String password = request.get("password");
//
//        // Hash password
//        String hashedPassword = new BCryptPasswordEncoder().encode(password);
//        String encryptedName = aesUtil.encrypt(name);
//
//        User user = new User();
//        user.setName(encryptedName);
//        user.setEmail(email);
//        user.setPassword(hashedPassword);
//
//        userRepository.save(user);
//        return ResponseEntity.ok("Signup successful!");
//    }
//    @PostMapping("/signup")
//    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
//        Map<String, String> response = new HashMap<>();
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            response.put("message", "Email already exists!");
//            return ResponseEntity.badRequest().body(response);
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//        response.put("message", "Signup successful!");
//        return ResponseEntity.ok(response);
//    }
//
//}
//

package com.ganesh.LifeStyleMatcherProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class SignUpController {

    @Autowired private UserRepository userRepository;
    @Autowired private AESUtil aesUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RestTemplate restTemplate;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;

    @Value("${ABSTRACT_API_KEY}")
    private String abstractApiKey;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");

        if (userRepository.findByEmail(email).isPresent()) {
            response.put("message", "Email already exists!");
            return ResponseEntity.badRequest().body(response);
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);

        userRepository.save(user);
        response.put("message", "Signup successful!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtpForSignup(@RequestBody Map<String, String> body) {
        Map<String, String> response = new HashMap<>();
        String email = body.get("email");

        // ✅ Check BEFORE external API
        if (userRepository.findByEmail(email).isPresent()) {
            response.put("message", "Email already exists!");
            return ResponseEntity.status(409).body(response);
        }

        // ✅ Only reach here if email doesn't exist
        String url = "https://emailvalidation.abstractapi.com/v1/?api_key=" + abstractApiKey + "&email=" + email;

        try {
            String result = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(result);

            String deliverability = json.getString("deliverability");

            if ("UNDELIVERABLE".equals(deliverability)) {
                response.put("message", "Invalid or non-existent email.");
                return ResponseEntity.badRequest().body(response);
            }

            String otp = String.valueOf((int)(Math.random() * 900000 + 100000));
            otpService.saveOtp(email, otp);
            emailService.sendSignupOtpEmail(email, otp);

            response.put("message", "OTP sent to your email.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error validating email.");
            return ResponseEntity.status(500).body(response);
        }
    }
    @PostMapping("/verify-signup-otp")
    public ResponseEntity<Map<String, String>> verifySignupOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        Map<String, String> response = new HashMap<>();

        if (otpService.verifyOtp(email, otp)) {
            otpService.clearOtp(email); // Optional: clear OTP after use
            response.put("message", "OTP verified successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid OTP.");
            return ResponseEntity.status(400).body(response);
        }
    }

}


