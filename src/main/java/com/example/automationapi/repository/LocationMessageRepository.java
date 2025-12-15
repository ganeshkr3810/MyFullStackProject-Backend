package com.example.automationapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.automationapi.model.LocationMessage;

import java.util.List;

public interface LocationMessageRepository extends JpaRepository<LocationMessage, Long> {
    List<LocationMessage> findByOwnerMobileOrderByCreatedAtDesc(String ownerMobile);
}
