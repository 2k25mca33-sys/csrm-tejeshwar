package com.example.demo.controller;

import com.example.demo.entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public List<AuditLog> getAuditLogs() {
        return entityManager.createQuery("SELECT a FROM AuditLog a ORDER BY a.timestamp DESC", AuditLog.class).getResultList();
    }
}
