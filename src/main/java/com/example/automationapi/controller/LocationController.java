package com.example.automationapi.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.LocationMessage;
import com.example.automationapi.model.ShareRequest;
import com.example.automationapi.repository.LocationMessageRepository;
import com.example.automationapi.repository.ShareRequestRepository;
import com.example.automationapi.service.NotificationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationMessageRepository locationRepo;
    private final ShareRequestRepository requestRepo;
    private final NotificationService notifier;

    public LocationController(
            LocationMessageRepository locationRepo,
            ShareRequestRepository requestRepo,
            NotificationService notifier) {
        this.locationRepo = locationRepo;
        this.requestRepo = requestRepo;
        this.notifier = notifier;
    }

    @PostMapping
    public ResponseEntity<Void> pushLocation(
            @RequestHeader("X-User-Mobile") String ownerMobile,
            @RequestBody LocationDto body) {

        // ✅ Save GPS to DB
        LocationMessage msg = new LocationMessage(
                ownerMobile,
                body.lat,
                body.lon,
                Instant.now()
        );
        locationRepo.save(msg);

        // ✅ VERY IMPORTANT FIX
        // ownerMobile = TARGET
        // requesterMobile = VIEWER
        List<ShareRequest> approvedRequests =
                requestRepo.findByTargetMobileAndStatus(
                        ownerMobile,
                        "APPROVED"
                );

        // ✅ Send GPS to ALL approved viewers
        for (ShareRequest r : approvedRequests) {
            notifier.notifyUser(
                    r.getRequesterMobile(), // 👈 viewer
                    "/queue/locations",
                    msg
            );
        }

        return ResponseEntity.ok().build();
    }

    static class LocationDto {
        public double lat;
        public double lon;
    }
}
