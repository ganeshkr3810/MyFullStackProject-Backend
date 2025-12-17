package com.example.automationapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.CurrentLocation;
import com.example.automationapi.model.ShareRequest;
import com.example.automationapi.repository.CurrentLocationRepository;
import com.example.automationapi.repository.ShareRequestRepository;
import com.example.automationapi.service.NotificationService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final CurrentLocationRepository locRepo;
    private final ShareRequestRepository reqRepo;
    private final NotificationService notifier;

    public LocationController(CurrentLocationRepository locRepo,
                              ShareRequestRepository reqRepo,
                              NotificationService notifier) {
        this.locRepo = locRepo;
        this.reqRepo = reqRepo;
        this.notifier = notifier;
    }

    @PostMapping
    public ResponseEntity<CurrentLocation> sendLocation(
            @RequestHeader("X-User-Mobile") String senderMobile,
            @RequestBody LocationDto body) {

        CurrentLocation location = locRepo
                .findById(senderMobile)
                .orElse(new CurrentLocation());

        location.setOwnerMobile(senderMobile);
        location.setLat(body.getLat());
        location.setLon(body.getLon());
        location.setTs(
            body.getTs() != null ? body.getTs() : Instant.now()
        );

        CurrentLocation saved = locRepo.save(location);

        // 🔴 TARGET sends → REQUESTER receives
        List<ShareRequest> approved =
            reqRepo.findByTargetMobileAndStatus(senderMobile, "APPROVED");

        for (ShareRequest r : approved) {
            notifier.notifyUser(
                r.getRequesterMobile(),   // 🔴 FIXED
                "/queue/locations",
                saved
            );
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/from/{ownerMobile}")
    public ResponseEntity<CurrentLocation> getCurrentLocation(
            @RequestHeader("X-User-Mobile") String caller,
            @PathVariable String ownerMobile) {

        boolean allowed =
            reqRepo.existsByRequesterMobileAndTargetMobileAndStatus(
                ownerMobile, caller, "APPROVED"
            );

        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        return locRepo.findById(ownerMobile)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    public static class LocationDto {
        private double lat;
        private double lon;
        private Instant ts;

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }
        public double getLon() { return lon; }
        public void setLon(double lon) { this.lon = lon; }
        public Instant getTs() { return ts; }
        public void setTs(Instant ts) { this.ts = ts; }
    }
}
