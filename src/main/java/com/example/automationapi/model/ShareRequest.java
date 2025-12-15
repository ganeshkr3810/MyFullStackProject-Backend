package com.example.automationapi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "share_request")
public class ShareRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_mobile", nullable = false)
    private String requesterMobile;

    @Column(name = "target_mobile", nullable = false)
    private String targetMobile;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, DECLINED, REVOKED

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    public ShareRequest() {}

    public ShareRequest(String requesterMobile, String targetMobile, String status) {
        this.requesterMobile = requesterMobile;
        this.targetMobile = targetMobile;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequesterMobile() { return requesterMobile; }
    public void setRequesterMobile(String requesterMobile) { this.requesterMobile = requesterMobile; }

    public String getTargetMobile() { return targetMobile; }
    public void setTargetMobile(String targetMobile) { this.targetMobile = targetMobile; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getRespondedAt() { return respondedAt; }
    public void setRespondedAt(Instant respondedAt) { this.respondedAt = respondedAt; }
}
