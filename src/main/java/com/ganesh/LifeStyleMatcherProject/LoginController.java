//package com.ganesh.LifeStyleMatcherProject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
//public class LoginController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AESUtil aesUtil;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String password = request.get("password");
//
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//        }
//
//        boolean isPasswordMatch = new BCryptPasswordEncoder().matches(password, user.getPassword());
//        if (!isPasswordMatch) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//
//        String decryptedName = aesUtil.decrypt(user.getName());
//        return ResponseEntity.ok("Welcome back, " + decryptedName + "!");
//    }
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData) {
//        String email = loginData.get("email");
//        String password = loginData.get("password");
//        Map<String, String> response = new HashMap<>();
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
//            response.put("message", "Login successful!");
//            return ResponseEntity.ok(response);
//        }
//        response.put("message", "Invalid credentials");
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
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
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AESUtil aesUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                String decryptedName = user.getName();
                response.put("message", "Welcome back, " + decryptedName + "!");
                return ResponseEntity.ok(response);
            }
        }

        response.put("message", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

