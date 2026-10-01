package com.example.demo;

import com.example.demo.dto.BookingRequest;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Resource;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ResourceRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingService bookingService;

    private User studentUser;
    private User adminUser;
    private Resource testResource;

    @BeforeEach
    void setUp() {
        studentUser = new User();
        studentUser.setId(1L);
        studentUser.setUsername("student1");
        studentUser.setRole(User.Role.ROLE_STUDENT);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin1");
        adminUser.setRole(User.Role.ROLE_ADMIN);

        testResource = new Resource();
        testResource.setId(1L);
        testResource.setName("Lab 101");
        testResource.setType(Resource.ResourceType.LAB);
    }

    @Test
    void testCreateBookingSuccess() {
        BookingRequest request = new BookingRequest();
        request.setResourceId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1).withHour(10));
        request.setEndTime(LocalDateTime.now().plusDays(1).withHour(12));

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(studentUser));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(bookingRepository.findOverlappingBookings(any(), any(), any())).thenReturn(Collections.emptyList());

        Booking savedBooking = new Booking();
        savedBooking.setId(100L);
        savedBooking.setResource(testResource);
        savedBooking.setUser(studentUser);
        savedBooking.setStatus(Booking.BookingStatus.UPCOMING);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        assertDoesNotThrow(() -> bookingService.createBooking(request, "student1"));
        
        verify(auditLogRepository, times(1)).save(any());
        verify(notificationService, times(1)).sendNotification(any(), any());
    }

    @Test
    void testCreateBookingOverlapRejection() {
        BookingRequest request = new BookingRequest();
        request.setResourceId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1).withHour(10));
        request.setEndTime(LocalDateTime.now().plusDays(1).withHour(12));

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(studentUser));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        
        Booking existingConflict = new Booking();
        when(bookingRepository.findOverlappingBookings(any(), any(), any())).thenReturn(List.of(existingConflict));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(request, "student1");
        });

        assertTrue(exception.getMessage().contains("already booked"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testUnauthorizedBookingModification() {
        BookingRequest request = new BookingRequest();
        request.setStartTime(LocalDateTime.now().plusDays(2).withHour(10));
        request.setEndTime(LocalDateTime.now().plusDays(2).withHour(12));

        Booking existingBooking = new Booking();
        existingBooking.setId(5L);
        existingBooking.setResource(testResource);
        User otherUser = new User();
        otherUser.setId(99L);
        existingBooking.setUser(otherUser);

        when(bookingRepository.findById(5L)).thenReturn(Optional.of(existingBooking));
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(studentUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.updateBookingTime(5L, request, "student1");
        });

        assertTrue(exception.getMessage().contains("Not authorized"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testBookingCancellation() {
        Booking existingBooking = new Booking();
        existingBooking.setId(5L);
        existingBooking.setResource(testResource);
        existingBooking.setUser(studentUser);

        when(bookingRepository.findById(5L)).thenReturn(Optional.of(existingBooking));
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(studentUser));

        bookingService.deleteBooking(5L, "student1");

        assertEquals(Booking.BookingStatus.CANCELLED, existingBooking.getStatus());
        verify(bookingRepository, times(1)).save(existingBooking);
        verify(auditLogRepository, times(1)).save(any());
    }
}
