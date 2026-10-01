package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Notification> getNotifications(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return entityManager.createQuery("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC", Notification.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @PutMapping("/{id}/read")
    @Transactional
    public ResponseEntity<?> markAsRead(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Notification notification = entityManager.find(Notification.class, id);
        
        if (notification == null) return ResponseEntity.notFound().build();
        if (!notification.getUser().getId().equals(user.getId())) return ResponseEntity.status(403).build();
        
        notification.setRead(true);
        entityManager.merge(notification);
        return ResponseEntity.ok().build();
    }
}
