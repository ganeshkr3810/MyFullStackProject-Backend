package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.automationapi.model.userRegistrationData;

public interface userRepository extends JpaRepository<userRegistrationData, Long> {
	
	

}
