package com.example.da1_backend.notification;

import com.example.da1_backend.notification.dto.NotificationRequest;
import com.example.da1_backend.user.UserService;
import com.example.da1_backend.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Notification> getUnreadNotifications(Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getCurrentUserByEmail(email);
        List<Notification> notis = service.getUnreadNotifications(user.getId());
        service.markAsRead(notis);
        return notis;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody NotificationRequest req) {
        Notification created = service.createNotification(req.getUserId(), req.getTitle(), req.getMessage());
        return ResponseEntity.ok(created);
    }
}
