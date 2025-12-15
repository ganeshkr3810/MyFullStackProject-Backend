package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.automationapi.model.CurrentLocation;

public interface CurrentLocationRepository
        extends JpaRepository<CurrentLocation, String> {
}
