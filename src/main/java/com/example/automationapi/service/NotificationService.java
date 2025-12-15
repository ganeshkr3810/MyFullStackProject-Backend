package com.example.automationapi.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyUser(String userMobile, String destination, Object payload) {
        simpMessagingTemplate.convertAndSendToUser(userMobile, destination, payload);
    }
}
