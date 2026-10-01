package com.example.demo.scheduler;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationService notificationService;

    // Run every hour
    @Scheduled(fixedRate = 3600000)
    public void sendBookingReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        
        // Find bookings that start between now and tomorrow (24h reminder window)
        // Ideally we would flag them as 'reminderSent' in DB to avoid duplicate sends,
        // but for this academic project we will simulate the check here.
        
        List<Booking> upcomingBookings = bookingRepository.findAll().stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.UPCOMING)
            .filter(b -> b.getStartTime().isAfter(now) && b.getStartTime().isBefore(tomorrow))
            .toList();

        for (Booking booking : upcomingBookings) {
            System.out.println("[SCHEDULER] Triggering reminder for booking ID " + booking.getId());
            // Realistically we'd only send it once. 
            // notificationService.sendNotification(booking.getUser(), "Reminder: Your booking for " + booking.getResource().getName() + " starts at " + booking.getStartTime());
        }
    }
}
