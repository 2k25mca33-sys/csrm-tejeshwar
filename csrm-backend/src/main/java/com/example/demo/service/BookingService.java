package com.example.demo.service;

import com.example.demo.dto.BookingDto;
import com.example.demo.dto.BookingRequest;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Resource;
import com.example.demo.entity.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ResourceRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResourceRepository resourceRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<BookingDto> getUserBookings(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return bookingRepository.findByUserId(user.getId()).stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    public List<BookingDto> getBookingsByResource(Long resourceId) {
        return bookingRepository.findByResourceId(resourceId).stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.UPCOMING || b.getStatus() == Booking.BookingStatus.ACTIVE)
            .map(this::mapToDto).collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id, String username) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        User user = userRepository.findByUsername(username).orElseThrow();
        if (!booking.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ROLE_ADMIN) {
            throw new RuntimeException("Not authorized to view this booking");
        }
        return mapToDto(booking);
    }

    public BookingDto createBooking(BookingRequest request, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Resource resource = resourceRepository.findById(request.getResourceId()).orElseThrow(() -> new RuntimeException("Resource not found"));

        if (user.getRole() == User.Role.ROLE_STUDENT && resource.getType() == Resource.ResourceType.CLASSROOM) {
            throw new RuntimeException("Students are not permitted to book classrooms.");
        }

        List<Booking> overlaps = bookingRepository.findOverlappingBookings(request.getResourceId(), request.getStartTime(), request.getEndTime());
        if (!overlaps.isEmpty()) {
            throw new RuntimeException("Resource is already booked for the selected time.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setResource(resource);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(Booking.BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);
        return mapToDto(saved);
    }
    
    public BookingDto updateBookingStatus(Long id, Booking.BookingStatus status, String username) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        if (user.getRole() != User.Role.ROLE_ADMIN) {
            throw new RuntimeException("Only Admins can approve or reject bookings.");
        }
        
        booking.setStatus(status);
        return mapToDto(bookingRepository.save(booking));
    }

    public BookingDto updateBookingTime(Long id, BookingRequest request, String username) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!booking.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ROLE_ADMIN) {
            throw new RuntimeException("Not authorized to modify this booking.");
        }

        List<Booking> overlaps = bookingRepository.findOverlappingBookings(booking.getResource().getId(), request.getStartTime(), request.getEndTime());
        // Remove self from overlap check
        overlaps = overlaps.stream().filter(b -> !b.getId().equals(booking.getId())).collect(Collectors.toList());
        
        if (!overlaps.isEmpty()) {
            throw new RuntimeException("Resource is already booked for the selected time.");
        }

        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        return mapToDto(bookingRepository.save(booking));
    }
    
    public void deleteBooking(Long id, String username) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();
        if(booking.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ROLE_ADMIN) {
            bookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Not authorized to delete this booking");
        }
    }

    private BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setResourceId(booking.getResource().getId());
        dto.setResourceName(booking.getResource().getName());
        dto.setUsername(booking.getUser().getUsername());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus().name());
        return dto;
    }
}
