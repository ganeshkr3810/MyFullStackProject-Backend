package com.example.automationapi.model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @Column(name = "mobile", nullable = false, length = 50)
    private String mobile;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User() {}

    public User(String mobile, String name) {
        this.mobile = mobile;
        this.name = name;
        this.createdAt = Instant.now();
    }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

