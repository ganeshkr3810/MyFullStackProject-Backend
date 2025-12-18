package com.example.automationapi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    private String mobile;

    private String name;
    private String email;
    private String address;

    @Column(nullable = false)
    private Instant createdAt;

    public User() {}

    public User(String mobile, String name, String email, String address) {
        this.mobile = mobile;
        this.name = name;
        this.email = email;
        this.address = address;
        this.createdAt = Instant.now();
    }

    public String getMobile() { return mobile; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}
