package com.example.automationapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.automationapi.model.User;

public interface LocationUserRepository extends JpaRepository<User, String> {
    // primary key is mobile (String)
}