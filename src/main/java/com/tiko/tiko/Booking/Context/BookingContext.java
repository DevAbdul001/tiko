package com.tiko.tiko.Booking.Context;

import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;

import java.util.Map;

public record BookingContext(
        Event event,
        Map<Long, EventTicketPrice> priceLookUp
) {
}
