package com.example.mini_saas.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending notifications.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notifyUser(Long userId, String message) {
        // TODO: Implement real notification logic
        // - Email via SendGrid/AWS SES
        // - Push notification via Firebase
        // - Webhook
        // - Kafka event

        log.info("NOTIFICATION to user {}: {}", userId, message);
    }
}