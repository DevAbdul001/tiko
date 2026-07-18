package com.tiko.tiko.Booking.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateBookingRequestDTO(
        Long eventId,
        String idempotencyKey,
        @NotEmpty
        List<@Valid BookingItemRequest> items
) {
        public int getTotalTickets(){
                return items.stream()
                        .mapToInt(BookingItemRequest::quantity)
                        .sum();
        }

}
