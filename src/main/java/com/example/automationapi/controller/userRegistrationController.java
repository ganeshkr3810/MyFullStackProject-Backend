package com.example.automationapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.automationapi.model.userRegistrationData;
import com.example.automationapi.repository.userRepository;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500") 
@RequestMapping("/api/userinfo")
public class userRegistrationController {
	 private final userRepository repository;
	   
	    public userRegistrationController(userRepository repository) {
	        this.repository = repository;    
	    }
	    
	    @GetMapping
	    public List<userRegistrationData> getAll() {
	        return repository.findAll();
	    }

	    // Get by ID
	    @GetMapping("/{id}")
	    public userRegistrationData getById(@PathVariable Long id) {
	        return repository.findById(id).orElse(null);
	    }
	  
	    @PostMapping
	    public userRegistrationData create(@RequestBody userRegistrationData data) { 
	    	return repository.save(data); 
	    	}


	    // Update existing
	    @PutMapping("/{id}")
	    public userRegistrationData update(@PathVariable Long id, @RequestBody userRegistrationData data) {
	    	userRegistrationData existing = repository.findById(id).orElse(null);
	        if (existing != null) {
	        	
	        	if(data.getName()!=null && !data.getName().isBlank()) { 
	        		existing.setName(data.getName());}
//	            existing.setName(data.getName());
	        	
	        	if(data.getAddress()!=null && !data.getAddress().isBlank()) {
	        		existing.setAddress(data.getAddress());	
	        	}
//	            existing.setAddress(data.getAddress());
	        	
	        	if(data.getEmail()!=null) {
	        		existing.setEmail(data.getEmail());	
	        	}
//	            existing.setEmail(data.getEmail());
	        	
	        	if(data.getPassword()!=null) {
	        		existing.setPassword(data.getPassword());	
	        	}
//	            existing.setPassword(data.getPassword());
	        	
	        	if(data.getProfession()!=null && !data.getProfession().isBlank()) {
	        		existing.setProfession(data.getProfession());	
	        	}
//	            existing.setProfession(data.getProfession());
	        	
	        	if(data.getStatus()!=null) {
	        		existing.setStatus(data.getStatus());	
	        	}
//	            existing.setStatus(data.getStatus());
	            return repository.save(existing);
	        }
	        return null;
	    }

	    // Delete by ID
	    @DeleteMapping("/{id}")
	    public String delete(@PathVariable Long id) {
	        repository.deleteById(id);
	        return "Deleted Successfully";
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody(required = false) userRegistrationData loginData) {
	        if (loginData == null || loginData.getEmail() == null || loginData.getPassword() == null) {
	            return ResponseEntity
	                    .badRequest()
	                    .body(Map.of("message", "Email and password are required"));
	        }

	        Optional<userRegistrationData> foundUser = repository.findAll().stream()
	                .filter(user -> user.getEmail().equals(loginData.getEmail())
	                             && user.getPassword().equals(loginData.getPassword()))
	                .findFirst();

	        if (foundUser.isPresent()) {
	            return ResponseEntity.ok(foundUser.get()); 
	        } else {
	            return ResponseEntity
	                    .status(401)
	                    .body(Map.of("message", "Invalid email or password"));
	        }
	    }



	    

}
