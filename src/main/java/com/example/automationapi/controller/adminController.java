package com.example.automationapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.automationapi.model.adminData;
import com.example.automationapi.repository.adminRepository;

@RestController
@CrossOrigin(origins = "*") 
@RequestMapping("/api/adminInfo")
public class adminController {
	 private final adminRepository repository;
	   
	    public adminController(adminRepository repository) {
	        this.repository = repository;    
	    }
	    
	    @GetMapping
	    public List<adminData> getAll() {
	        return repository.findAll();
	    }

	    // Get by ID
	    @GetMapping("/{id}")
	    public adminData getById(@PathVariable Long id) {
	        return repository.findById(id).orElse(null);
	    }
}
