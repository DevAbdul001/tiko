package com.tiko.tiko.Booking.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateBookingRequestDTO(
        Long eventId,

        @NotEmpty
        List<@Valid BookingItemRequest> items
) {}
