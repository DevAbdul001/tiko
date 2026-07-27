package com.tiko.tiko.TestDataFactory;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.EventTicketPrice;

public final class BookingItemFactory {

    private BookingItemFactory() {
    }

    public static BookingItem create(
            Booking booking,
            EventTicketPrice eventTicketPrice) {

        BookingItem item = new BookingItem(
                1,
                eventTicketPrice.getPrice(),
                eventTicketPrice.getTicketType().getName(),
                null,
                eventTicketPrice
        );

        booking.addBookingItem(item);

        return item;
    }

    public static BookingItem create(
            Booking booking,
            EventTicketPrice eventTicketPrice,
            int quantity) {

        BookingItem item = new BookingItem(
                quantity,
                eventTicketPrice.getPrice(),
                eventTicketPrice.getTicketType().getName(),
                null,
                eventTicketPrice
        );

        booking.addBookingItem(item);

        return item;
    }

    public static BookingItem create(
            Booking booking,
            EventTicketPrice eventTicketPrice,
            int quantity,
            Long unitPrice) {

        BookingItem item = new BookingItem(
                quantity,
                unitPrice,
                eventTicketPrice.getTicketType().getName(),
                null,
                eventTicketPrice
        );

        booking.addBookingItem(item);

        return item;
    }
}