package com.tiko.tiko.TestDataFactory;

import com.tiko.tiko.Events.Entity.TicketType;

import java.util.UUID;

public final class TicketTypeFactory {

    private TicketTypeFactory() {
    }

    public static TicketType create() {
        return new TicketType(
                "Ticket-" + UUID.randomUUID()
        );
    }

    public static TicketType create(String name) {
        return new TicketType(name);
    }
}