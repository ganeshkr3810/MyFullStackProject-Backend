package com.example.automationapi.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.CurrentLocation;
import com.example.automationapi.model.ShareRequest;
import com.example.automationapi.repository.CurrentLocationRepository;
import com.example.automationapi.repository.ShareRequestRepository;
import com.example.automationapi.service.NotificationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final CurrentLocationRepository currentRepo;
    private final ShareRequestRepository requestRepo;
    private final NotificationService notifier;

    public LocationController(
            CurrentLocationRepository currentRepo,
            ShareRequestRepository requestRepo,
            NotificationService notifier) {
        this.currentRepo = currentRepo;
        this.requestRepo = requestRepo;
        this.notifier = notifier;
    }

    @PostMapping
    public ResponseEntity<Void> pushLocation(
            @RequestHeader("X-User-Mobile") String owner,
            @RequestBody LocationDto body) {

        Instant now = Instant.now();

        // 🔴 RATE LIMIT (2 seconds)
        currentRepo.findById(owner).ifPresent(prev -> {
            if (Duration.between(prev.getTs(), now).getSeconds() < 2) {
                throw new RuntimeException("RATE_LIMIT");
            }
        });

        // 🔴 UPSERT CURRENT LOCATION
        CurrentLocation loc = new CurrentLocation();
        loc.setOwnerMobile(owner);
        loc.setLat(body.lat);
        loc.setLon(body.lon);
        loc.setTs(now);
        currentRepo.save(loc);

        // 🔴 SEND ONLY TO STILL-APPROVED VIEWERS
        List<ShareRequest> approved =
                requestRepo.findByTargetMobileAndStatus(owner, "APPROVED");

        for (ShareRequest r : approved) {
            notifier.notifyUser(
                    r.getRequesterMobile(),
                    "/queue/locations",
                    loc
            );
        }

        return ResponseEntity.ok().build();
    }
    
    public static class LocationDto {
        public double lat;
        public double lon;
    }
}
