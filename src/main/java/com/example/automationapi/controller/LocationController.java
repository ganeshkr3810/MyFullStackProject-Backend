package com.example.automationapi.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.LocationMessage;
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

        // save location
        LocationMessage msg = new LocationMessage(
                ownerMobile,
                body.lat,
                body.lon,
                Instant.now()
        );
        locationRepo.save(msg);

        // send live location ONLY to approved viewers
        List<com.example.automationapi.model.ShareRequest> approved =
                requestRepo.findByRequesterMobileAndStatusIn(
                        ownerMobile,
                        List.of("APPROVED")
                );

        for (var r : approved) {
            notifier.notifyUser(
                    r.getTargetMobile(),
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
