package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.automationapi.model.adminData;

public interface adminRepository extends JpaRepository<adminData, Long> {

	 adminData findByEmail(String email);
}
