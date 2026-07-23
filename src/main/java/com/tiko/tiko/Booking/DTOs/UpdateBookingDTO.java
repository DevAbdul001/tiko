package com.tiko.tiko.Booking.DTOs;

import java.util.List;

public record UpdateBookingDTO(
        Long bookingId,
        List<BookingItemUpdateDTO> items
) {}
