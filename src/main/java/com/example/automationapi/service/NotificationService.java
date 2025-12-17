package com.example.automationapi.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate template;

    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notifyUser(String mobile, String destination, Object payload) {
        template.convertAndSendToUser(
                mobile,
                destination,
                payload
        );
    }
}

