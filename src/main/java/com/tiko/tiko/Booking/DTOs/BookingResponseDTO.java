package com.tiko.tiko.Booking.DTOs;

import java.time.LocalDateTime;

public record BookingResponseDTO(
        Long bookingId,
        String bookingRef,
        String userName,
        String eventName,
        Long totalAmount,
        LocalDateTime createdAt
) {}
