package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Long totalUsers = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        Long totalResources = (Long) entityManager.createQuery("SELECT COUNT(r) FROM Resource r").getSingleResult();
        Long totalBookings = (Long) entityManager.createQuery("SELECT COUNT(b) FROM Booking b").getSingleResult();
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalResources", totalResources);
        stats.put("totalBookings", totalBookings);
        
        return stats;
    }
}
