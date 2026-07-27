package com.tiko.tiko.TestDataFactory;


import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Users.Entity.User;

import java.time.LocalDateTime;

public final class EventFactory {

    private EventFactory() {
    }

    public static Event create(User organizer, EventCategory category) {
        return new Event(
                "Test Event",
                organizer,
                LocalDateTime.now().plusDays(30),
                category,
                100,
                "Nairobi"
        );
    }

    public static Event create(
            String name,
            User organizer,
            EventCategory category) {

        return new Event(
                name,
                organizer,
                LocalDateTime.now().plusDays(30),
                category,
                100,
                "Nairobi"
        );
    }

    public static Event create(
            String name,
            User organizer,
            EventCategory category,
            LocalDateTime date,
            int capacity,
            String location) {

        return new Event(
                name,
                organizer,
                date,
                category,
                capacity,
                location
        );
    }
}