package com.example.automationapi.service;

import org.springframework.stereotype.Service;

import com.example.automationapi.model.AutomationData;
import com.example.automationapi.repository.AutomationRepository;

@Service
public class AutomationService {

    private final AutomationRepository repository;

    public AutomationService(AutomationRepository repository) {
        this.repository = repository;
    }

//    // Combine username and password, save into new column
//    public AutomationData combineUsernamePassword(Long id) {
//        AutomationData data = repository.findById(id).orElse(null);
//        if (data != null) {
//            String combined = data.getUsername() + "_" + data.getPassword();
//            data.setUser_pass_combined(combined);
//            return repository.save(data);
//        }
//        return null;
//    }

    // ✅ New method for POST create
    public AutomationData createData(AutomationData data) {
        if (data.getUsername() != null && data.getPassword() != null) {
            data.setUser_pass_combined(data.getUsername() + "_" + data.getPassword());
        }
        return repository.save(data);
    }
}
