package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
