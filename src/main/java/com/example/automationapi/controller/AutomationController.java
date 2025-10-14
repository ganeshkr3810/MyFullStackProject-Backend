package com.example.automationapi.controller;

import com.example.automationapi.model.AutomationData;
import com.example.automationapi.repository.AutomationRepository;
import com.example.automationapi.service.AutomationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class AutomationController {

   
    private final AutomationRepository repository;
    private final AutomationService service;

    public AutomationController(AutomationRepository repository, AutomationService service) {
        this.repository = repository;
        this.service = service;
    }
    
    // Get all rows
    @GetMapping
    public List<AutomationData> getAll() {
        return repository.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public AutomationData getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
  
    @PostMapping
    public AutomationData create(@RequestBody AutomationData data) {
        return service.createData(data);
    }


    // Update existing
    @PutMapping("/{id}")
    public AutomationData update(@PathVariable Long id, @RequestBody AutomationData data) {
        AutomationData existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setUrl(data.getUrl());
            existing.setUsername(data.getUsername());
            existing.setPassword(data.getPassword());
            existing.setServer(data.getServer());
            existing.setTanent(data.getTanent());
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
    
    
}
