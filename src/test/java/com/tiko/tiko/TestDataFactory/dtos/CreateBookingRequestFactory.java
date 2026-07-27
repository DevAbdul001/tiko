package com.tiko.tiko.TestDataFactory.dtos;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;

import java.util.List;
import java.util.UUID;

public final class CreateBookingRequestFactory {

    private CreateBookingRequestFactory() {
    }

    public static CreateBookingRequestDTO create(
            Long eventId,
            Long eventTicketPriceId) {

        return new CreateBookingRequestDTO(
                eventId,
                UUID.randomUUID().toString(),
                List.of(
                        BookingItemRequestFactory.create(eventTicketPriceId)
                )
        );
    }

    public static CreateBookingRequestDTO create(
            Long eventId,
            List<BookingItemRequest> items) {

        return new CreateBookingRequestDTO(
                eventId,
                UUID.randomUUID().toString(),
                items
        );
    }

    public static CreateBookingRequestDTO create(
            Long eventId,
            String idempotencyKey,
            List<BookingItemRequest> items) {

        return new CreateBookingRequestDTO(
                eventId,
                idempotencyKey,
                items
        );
    }
}