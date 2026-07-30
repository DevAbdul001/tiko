package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class BookingValidationService {

    @Autowired
    private EventTicketPriceRepository eventTicketPriceRepository;

    @Autowired
    private EventsRepo eventsRepo;

    public void validate(
            Event event,
            EventTicketPrice eventTicketPrice,
            CreateBookingRequestDTO request
    ){
        validateEventIsActive(event);
        List<EventTicketPrice> ticketPriceList = getEventTicketPrice(eventTicketPrice.getId(), event);
        boolean ticketAvailable = validateTicketTypeAvailability(
                ticketPriceList, request.items())
        )

        return;
    }

    private List<EventTicketPrice>  getEventTicketPrice(Long eventTicketPriceId, Event event ){
        return event.getEventTicketPriceList();
    }

    public void validateEventIsActive(Event event){
        if(event.getStatus() != EventStatus.PUBLISHED){
            throw new RuntimeException("Event is not available for booking");
        }
    }

    public boolean validateTicketTypeAvailability(List<EventTicketPrice> eventTicketPrices, List<EventTicketPrice> requestItems){

        return new HashSet<>(requestItems).containsAll(eventTicketPrices);
    }

    public void validateTicketTypePriceBelongsToEvent(EventTicketPrice eventTicketPrice, Event event){
        Long eventTicketPriceId = eventTicketPrice.getId();
        Long eventId = event.getId();
        if (!eventTicketPriceId.equals(eventId)){
            throw new RuntimeException("Ticket price does not belong to event");
        }
    }
}
