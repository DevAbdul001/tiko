package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookingContextLoader {

    @Autowired
    private EventsRepo eventsRepo;

    public BookingContext load(Long eventId) {
        Event event = eventsRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Invalid event id"));

        Map<Long, EventTicketPrice> lookup =
                event.getEventTicketPriceList()
                        .stream()
                        .collect(Collectors.toMap(
                                EventTicketPrice::getId,
                                Function.identity()
                        ));

        return new BookingContext(event, lookup);
    }
}
