package com.example.demo.controller;

import com.example.demo.entity.ServiceRequest;
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
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<ServiceRequest> getServiceRequests(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        
        if (user.getRole() == User.Role.ROLE_ADMIN) {
            return entityManager.createQuery("SELECT s FROM ServiceRequest s ORDER BY s.createdAt DESC", ServiceRequest.class).getResultList();
        } else {
            return entityManager.createQuery("SELECT s FROM ServiceRequest s WHERE s.user.id = :userId ORDER BY s.createdAt DESC", ServiceRequest.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ServiceRequest> createServiceRequest(@RequestBody ServiceRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        request.setUser(user);
        request.setStatus("PENDING");
        entityManager.persist(request);
        return ResponseEntity.ok(request);
    }
}
