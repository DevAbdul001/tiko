package com.tiko.tiko.Booking.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingItemRequest(
        @NotNull
        Long eventTicketPriceId,

        @Positive
        Long quantity
) {
}
