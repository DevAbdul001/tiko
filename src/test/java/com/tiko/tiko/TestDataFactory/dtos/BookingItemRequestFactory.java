package com.tiko.tiko.TestDataFactory.dtos;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;

public final class BookingItemRequestFactory {

    private BookingItemRequestFactory() {
    }

    public static BookingItemRequest create(Long eventTicketPriceId) {
        return new BookingItemRequest(
                eventTicketPriceId,
                1
        );
    }

    public static BookingItemRequest create(
            Long eventTicketPriceId,
            int quantity) {

        return new BookingItemRequest(
                eventTicketPriceId,
                quantity
        );
    }
}
