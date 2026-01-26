package com.example.mini_saas.notifications;

import com.example.mini_saas.tasks.event.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Application-level event listener.
 */
@Component
public class TaskEventListener {

    private static final Logger log = LoggerFactory.getLogger(TaskEventListener.class);

    private final NotificationService notificationService;

    public TaskEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    @Async
    public void handleTaskCreated(TaskCreatedEvent event) {
        try {
            log.debug("Handling TaskCreatedEvent: taskId={}", event.taskId());

            String message = String.format(
                    "Task created: %s (ID: %d)",
                    event.title(),
                    event.taskId()
            );

            notificationService.notifyUser(event.ownerId(), message);

        } catch (Exception e) {
            log.error("Failed to handle TaskCreatedEvent: taskId={}", event.taskId(), e);
        }
    }
}
