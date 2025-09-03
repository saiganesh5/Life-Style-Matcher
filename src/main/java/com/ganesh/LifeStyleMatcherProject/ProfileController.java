//package com.ganesh.LifeStyleMatcherProject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//// ProfileController.java
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
//public class ProfileController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AESUtil aesUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // GET: Fetch user details by email
//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(@RequestParam String email) {
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        User user = userOpt.get();
//        Map<String, String> userData = new HashMap<>();
//        userData.put("name", aesUtil.decrypt(user.getName()));
//        userData.put("email", user.getEmail());
//        return ResponseEntity.ok(userData);
//    }
//
//    // PUT: Update user profile
//    @PutMapping("/profile")
//    public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Map<String, String> request) {
//        Map<String, String> response = new HashMap<>();
//        String email = request.get("email");
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            response.put("message", "User not found");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        User user = userOpt.get();
//
//        // Update name
//        String newName = request.get("name");
//        user.setName(aesUtil.encrypt(newName));
//
//        // Update password if provided
//        String newPassword = request.get("password");
//        if (newPassword != null && !newPassword.isBlank()) {
//            user.setPassword(passwordEncoder.encode(newPassword));
//        }
//
//        userRepository.save(user);
//        response.put("message", "Profile updated successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<?> getUser(@RequestParam String email) {
//        return userRepository.findByEmail(email)
//                .map(user -> {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("email", user.getEmail());
//                    map.put("name", aesUtil.decrypt(user.getName()));
//                    return ResponseEntity.ok(map);
//                }).orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/update-profile")
//    public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Map<String, String> data) {
//        Map<String, String> response = new HashMap<>();
//
//        String email = data.get("email");
//        String newName = data.get("name");
//        String newPassword = data.get("password");
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            response.put("message", "User not found");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        User user = userOpt.get();
//
//        if (newName != null && !newName.isBlank()) {
//            user.setName(aesUtil.encrypt(newName));
//        }
//
//        if (newPassword != null && !newPassword.isBlank()) {
//            user.setPassword(passwordEncoder.encode(newPassword));
//        }
//
//        userRepository.save(user);
//        response.put("message", "Profile updated successfully!");
//        return ResponseEntity.ok(response);
//    }
//
//
//
//
//}
//
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
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AESUtil aesUtil;

@Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ GET: Fetch user details by email (for profile display)
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getProfile(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        response.put("name", aesUtil.decrypt(user.getName()));
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    }


    // ✅ PUT: Update profile info
    @PutMapping("/update-profile")
    public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();

        // Update name
        String newName = request.get("name");
        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }

        // Update password if present
        String newPassword = request.get("password");
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        response.put("message", "Profile updated successfully!");
        return ResponseEntity.ok(response);
    }
}
