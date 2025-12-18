package com.example.automationapi.controller;

import org.springframework.web.bind.annotation.*;

import com.example.automationapi.repository.CurrentLocationRepository;
import com.example.automationapi.service.NotificationService;

@RestController
@RequestMapping("/api/view")
public class LocationViewController {

    private final NotificationService notifier;
    private final CurrentLocationRepository locationRepo;

    public LocationViewController(
            NotificationService notifier,
            CurrentLocationRepository locationRepo) {
        this.notifier = notifier;
        this.locationRepo = locationRepo;
    }

    // Viewer requests last location of owner
    @PostMapping("/last-location")
    public void sendLastLocation(
            @RequestHeader("X-User-Mobile") String viewer,
            @RequestBody ViewDto body) {

        notifier.sendLastLocationIfExists(
                viewer,
                body.ownerMobile,
                locationRepo
        );
    }

    static class ViewDto {
        public String ownerMobile;
    }
}
