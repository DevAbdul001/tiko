package com.tiko.tiko.Booking.DTOs;

import java.util.List;

public record UpdateBookingItemDTO(
        int quantity,
        Long bookingId,
        List<Long> itemIds
) {}
