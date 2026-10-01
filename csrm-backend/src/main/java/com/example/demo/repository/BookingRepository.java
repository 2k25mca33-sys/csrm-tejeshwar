package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByResourceId(Long resourceId);
    
    @Query("SELECT b FROM Booking b WHERE b.resource.id = :resourceId AND b.status IN ('UPCOMING', 'ACTIVE') AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findOverlappingBookings(@Param("resourceId") Long resourceId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}

