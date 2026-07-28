package com.tiko.tiko.TestDataFactory.Scenerios;

import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.TestDataFactory.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

public class BookingItemScenarioFactory {

    public static BookingItemScenario create(TestEntityManager em) {

        BookingScenario bookingScenario = BookingScenarioFactory.create(em);

        BookingItem bookingItem = BookingItemFactory.create(
                bookingScenario.booking(),
                bookingScenario.ticketPrice()
        );

        em.persist(bookingItem);

        bookingScenario.booking().addBookingItem(bookingItem);

        em.flush();

        return new BookingItemScenario(
                bookingScenario.organizer(),
                bookingScenario.customer(),
                bookingScenario.category(),
                bookingScenario.ticketType(),
                bookingScenario.event(),
                bookingScenario.ticketPrice(),
                bookingScenario.booking(),
                bookingItem
        );
    }
}