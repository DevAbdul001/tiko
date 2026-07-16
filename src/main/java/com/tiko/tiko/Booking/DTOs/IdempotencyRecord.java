package com.tiko.tiko.Booking.DTOs;

import com.tiko.tiko.Booking.Enums.IdempotencyStatus;

public record IdempotencyRecord(
        String key,
        IdempotencyStatus status,
        Long bookingId,
        String bookingRef
) {}
