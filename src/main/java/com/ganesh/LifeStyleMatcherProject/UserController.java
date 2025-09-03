//package com.ganesh.LifeStyleMatcherProject;
//
//import com.ganesh.LifeStyleMatcherProject.User;
//import com.ganesh.LifeStyleMatcherProject.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/auth")
//public class UserController {
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @PostMapping("/signup")
//    public String signup(@RequestBody User user) {
//        if (userRepo.findByEmail(user.getEmail()) != null) {
//            return "User already exists!";
//        }
//        userRepo.save(user);
//        return "Signup successful!";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        User dbUser = userRepo.findByEmail(user.getEmail());
//        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
//            return "Login successful!";
//        } else {
//            return "Invalid email or password!";
//        }
//    }
//}
