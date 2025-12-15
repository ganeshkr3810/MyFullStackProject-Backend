package com.example.automationapi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "current_location")
public class CurrentLocation {

    @Id
    @Column(name = "owner_mobile", nullable = false)
    private String ownerMobile;

    private double lat;
    private double lon;

    private Instant ts;

    public String getOwnerMobile() { return ownerMobile; }
    public void setOwnerMobile(String ownerMobile) { this.ownerMobile = ownerMobile; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public Instant getTs() { return ts; }
    public void setTs(Instant ts) { this.ts = ts; }
}
