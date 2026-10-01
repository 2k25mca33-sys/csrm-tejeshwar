package com.example.demo.controller;

import com.example.demo.dto.BookingDto;
import com.example.demo.dto.BookingRequest;
import com.example.demo.entity.Booking;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/my")
    public List<BookingDto> getMyBookings(Authentication authentication) {
        return bookingService.getUserBookings(authentication.getName());
    }

    @GetMapping("/resource/{id}")
    public List<BookingDto> getBookingsByResource(@PathVariable Long id) {
        return bookingService.getBookingsByResource(id);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request, Authentication authentication) {
        try {
            return ResponseEntity.ok(bookingService.createBooking(request, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam Booking.BookingStatus status, Authentication authentication) {
        try {
            return ResponseEntity.ok(bookingService.updateBookingStatus(id, status, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/time")
    public ResponseEntity<?> updateBookingTime(@PathVariable Long id, @RequestBody BookingRequest request, Authentication authentication) {
        try {
            return ResponseEntity.ok(bookingService.updateBookingTime(id, request, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id, Authentication authentication) {
        try {
            bookingService.deleteBooking(id, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
