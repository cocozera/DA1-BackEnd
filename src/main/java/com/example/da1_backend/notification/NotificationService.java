package com.example.da1_backend.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repo;

    public List<Notification> getUnreadNotifications(Long userId) {
        return repo.findByUserIdAndReadFalse(userId);
    }

    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(n -> n.setRead(true));
        repo.saveAll(notifications);
    }

    public Notification createNotification(Long userId, String title, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        return repo.save(n);
    }
}
