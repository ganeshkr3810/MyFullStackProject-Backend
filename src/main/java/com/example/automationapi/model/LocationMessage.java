package com.example.automationapi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "location_message")
public class LocationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_mobile", nullable = false)
    private String ownerMobile;

    private double lat;
    private double lon;

    @Column(name = "ts", nullable = false)
    private Instant ts;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public LocationMessage() {}

    public LocationMessage(String ownerMobile, double lat, double lon, Instant ts) {
        this.ownerMobile = ownerMobile;
        this.lat = lat;
        this.lon = lon;
        this.ts = ts;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerMobile() { return ownerMobile; }
    public void setOwnerMobile(String ownerMobile) { this.ownerMobile = ownerMobile; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public Instant getTs() { return ts; }
    public void setTs(Instant ts) { this.ts = ts; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}