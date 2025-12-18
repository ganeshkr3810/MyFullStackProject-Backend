package com.example.automationapi.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.User;
import com.example.automationapi.repository.LocationUserRepository;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final LocationUserRepository repo;

    public UserController(LocationUserRepository repo) {
        this.repo = repo;
    }

    // ---------- REGISTER ----------
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDto dto) {
        if (repo.existsById(dto.mobile)) {
            return ResponseEntity.status(409).build();
        }
        User u = new User(dto.mobile, dto.name, dto.email, dto.address);
        repo.save(u);
        return ResponseEntity.ok(u);
    }

    // ---------- LOGIN ----------
    @GetMapping("/login/{mobile}")
    public ResponseEntity<User> login(@PathVariable String mobile) {
        Optional<User> u = repo.findById(mobile);
        return u.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    static class RegisterDto {
        public String name;
        public String email;
        public String mobile;
        public String address;
    }
}
