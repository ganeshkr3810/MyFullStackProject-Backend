package com.example.automationapi.repository;

import com.example.automationapi.model.AutomationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomationRepository extends JpaRepository<AutomationData, Long> {
}