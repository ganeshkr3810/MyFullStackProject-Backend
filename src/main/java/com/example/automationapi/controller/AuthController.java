package com.example.automationapi.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.automationapi.model.adminData;
import com.example.automationapi.model.userRegistrationData;
import com.example.automationapi.repository.adminRepository;
import com.example.automationapi.repository.userRepository;
import com.example.automationapi.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private adminRepository repository;
    
    @Autowired
    private userRepository userrepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/adminlogin")
    public ResponseEntity<?> adminlogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // 1️⃣ Validate input first
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        // 2️⃣ Find user by email
        adminData user = repository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        }

        // 3️⃣ Validate password (in real app, use BCrypt)
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
        }

        // 4️⃣ Generate JWT token
        String token = jwtUtil.generateToken(email);

        // 5️⃣ Optional: Save token in DB (add a jwtToken field in adminData entity)
        user.setJwtToken(token);
        repository.save(user);

        // 6️⃣ Return response
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "token", token,
            "email", email
        ));
    }
    
    @PostMapping("/userlogin")
    public ResponseEntity<?> userlogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // 1️⃣ Validate input first
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        // 2️⃣ Find user by email
        userRegistrationData user = userrepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        }

        // 3️⃣ Validate password (in real app, use BCrypt)
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
        }

        // 4️⃣ Generate JWT token
        String token = jwtUtil.generateToken(email);

        // 5️⃣ Optional: Save token in DB (add a jwtToken field in adminData entity)
        user.setJwtToken(token);
        userrepository.save(user);

        // 6️⃣ Return response
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "token", token,
            "email", email
        ));
    }
    
    

}
