package com.example.automationapi.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.automationapi.model.CurrentLocation;
import com.example.automationapi.repository.CurrentLocationRepository;

@Service
public class NotificationService {

    private final SimpMessagingTemplate template;

    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    // 🔔 Generic user notification (already used by you)
    public void notifyUser(String mobile, String destination, Object payload) {
        System.out.println(
            "📡 WS SEND → user=[" + mobile + "] destination=[" + destination + "]"
        );

        template.convertAndSendToUser(
                mobile,
                destination,
                payload
        );
    }

    // 📍 NEW: Send last known location when viewer connects
    public void sendLastLocationIfExists(
            String viewerMobile,
            String ownerMobile,
            CurrentLocationRepository locationRepo) {

        locationRepo.findById(ownerMobile).ifPresent(loc -> {
            System.out.println(
                "📍 Sending LAST location of " + ownerMobile +
                " to viewer " + viewerMobile
            );

            template.convertAndSendToUser(
                    viewerMobile,
                    "/queue/locations",
                    loc
            );
        });
    }
}
