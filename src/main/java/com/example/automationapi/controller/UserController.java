package com.example.automationapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.User;
import com.example.automationapi.repository.LocationUserRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LocationUserRepository repo;

    public UserController(LocationUserRepository repo) {
        this.repo = repo;
    }

    // register or upsert a user
    @PostMapping
    public ResponseEntity<User> register(@RequestBody RegisterDto body) {
        if (body.getMobile() == null || body.getMobile().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        User u = new User(body.getMobile(), body.getName());
        User saved = repo.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getMobile())).body(saved);
    }

    @GetMapping("/{mobile}")
    public ResponseEntity<User> get(@PathVariable String mobile) {
        Optional<User> opt = repo.findById(mobile);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class RegisterDto {
        private String mobile;
        private String name;
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}

