package com.tiko.tiko.TestDataFactory.Scenerios;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Users.Entity.User;

public record BookingScenario(

        User organizer,
        User customer,
        EventCategory category,
        TicketType ticketType,
        Event event,
        EventTicketPrice ticketPrice,
        Booking booking
) {}
