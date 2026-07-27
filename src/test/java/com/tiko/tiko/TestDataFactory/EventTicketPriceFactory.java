package com.tiko.tiko.TestDataFactory;

import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;

public final class EventTicketPriceFactory {

    private EventTicketPriceFactory() {
    }

    public static EventTicketPrice create(Event event, TicketType ticketType) {
        EventTicketPrice ticketPrice = new EventTicketPrice(
                5000L,
                100,
                ticketType
        );

        ticketPrice.setEvent(event);
        ticketPrice.initializeRemainingTickets();

        return ticketPrice;
    }

    public static EventTicketPrice create(
            Event event,
            TicketType ticketType,
            Long price,
            int quantity) {

        EventTicketPrice ticketPrice = new EventTicketPrice(
                price,
                quantity,
                ticketType
        );

        ticketPrice.setEvent(event);
        ticketPrice.initializeRemainingTickets();

        return ticketPrice;
    }
}