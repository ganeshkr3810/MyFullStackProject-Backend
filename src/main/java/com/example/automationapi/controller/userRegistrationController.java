package com.example.automationapi.controller;

import com.example.automationapi.model.userRegistrationData;
import com.example.automationapi.repository.userRepository;
import com.example.automationapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/userinfo")
public class userRegistrationController {

	private final userRepository repository;

	@Autowired
	private JwtUtil jwtUtil;

	public userRegistrationController(userRepository repository) {
		this.repository = repository;
	}


	@GetMapping
	public List<userRegistrationData> getAll() {
		return repository.findAll();
	}


	@GetMapping("/{id}")
	public userRegistrationData getById(@PathVariable Long id) {
		return repository.findById(id).orElse(null);
	}


	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody userRegistrationData data) {
		if (data.getEmail() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email required"));
		}
		if(data.getPassword() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "password required"));
		}

		Optional<userRegistrationData> existingUser = repository.findAll().stream()
				.filter(u -> u.getEmail().equalsIgnoreCase(data.getEmail())).findFirst();

		if (existingUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists"));
		}

		// Generate JWT
		String token = jwtUtil.generateToken(data.getEmail());
		data.setJwtToken(token);

		userRegistrationData savedUser = repository.save(data);

		return ResponseEntity
				.ok(Map.of("message", "Registration successful", "email", savedUser.getEmail()));
	}


	@PostMapping("/login")
	public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "Authorization token missing"));
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired token"));
		}

		String email = jwtUtil.extractEmail(token);

		return ResponseEntity.ok(Map.of("message", "Login successful", "email", email));
	}


	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody userRegistrationData data) {
		Optional<userRegistrationData> optionalUser = repository.findById(id);
		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
		}

		userRegistrationData existing = optionalUser.get();

		if (data.getName() != null && !data.getName().isBlank())
			existing.setName(data.getName());
		if (data.getAddress() != null && !data.getAddress().isBlank())
			existing.setAddress(data.getAddress());
		if (data.getProfession() != null && !data.getProfession().isBlank())
			existing.setProfession(data.getProfession());
		if (data.getStatus() != null && !data.getStatus().isBlank())
			existing.setStatus(data.getStatus());
		if (data.getPassword() != null && !data.getPassword().isBlank())
			existing.setPassword(data.getPassword());

		repository.save(existing);
		return ResponseEntity.ok(Map.of("message", "User updated successfully"));
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
		}
		repository.deleteById(id);
		return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
	}
}
