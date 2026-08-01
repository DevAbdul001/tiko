package com.tiko.tiko.TestDataFactory.Scenerios;

import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Users.Entity.User;

public record EventScenario(
        User organizer,
        EventCategory category,
        TicketType ticketType,
        Event event,
        EventTicketPrice ticketPrice
) {}