package com.tiko.tiko.TestDataFactory;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Enums.BookingStatus;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Users.Entity.User;

import java.util.UUID;

public final class BookingFactory {

    private BookingFactory() {
    }

    public static Booking create(User user, Event event) {

        Booking booking = new Booking(
                "BOOK-" + UUID.randomUUID(),
                5000L,
                event,
                user
        );

        booking.setStatus(BookingStatus.PENDING);

        return booking;
    }

    public static Booking create(
            User user,
            Event event,
            Long totalAmount) {

        Booking booking = new Booking(
                "BOOK-" + UUID.randomUUID(),
                totalAmount,
                event,
                user
        );

        booking.setStatus(BookingStatus.PENDING);

        return booking;
    }

    public static Booking create(
            String bookingRef,
            User user,
            Event event,
            Long totalAmount,
            BookingStatus status) {

        Booking booking = new Booking(
                bookingRef,
                totalAmount,
                event,
                user
        );

        booking.setStatus(status);

        return booking;
    }
}