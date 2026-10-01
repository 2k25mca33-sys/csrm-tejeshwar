package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(User user, String message) {
        // 1. In-App Notification (Always execute)
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);

        // 2. Email / SMS Integration Placeholder
        // Architecture is ready for external providers (e.g. AWS SES, Twilio, SendGrid)
        // System logs the intent to send rather than pretending it was actually sent.
        System.out.println("[NOTIFICATION SERVICE] In-app notification saved for user: " + user.getUsername());
        System.out.println("[NOTIFICATION SERVICE] External Email/SMS providers are not currently configured. Skipping external delivery for message: " + message);
    }
}
