package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.Events.Repository.EventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class BookingValidationService {

    @Autowired
    private EventsRepo eventsRepo;

    public void validate(
            CreateBookingRequestDTO requestDTO
    ){

        if (requestDTO.items().isEmpty()) {
            throw new RuntimeException("Booking must contain at least one ticket");
        }


        Event event = eventsRepo.findById(requestDTO.eventId())
                .orElseThrow(()-> new RuntimeException("Invalid event id"));
        List<EventTicketPrice> ticketPrices = event.getEventTicketPriceList();

        Map<Long, EventTicketPrice> priceLookup =
                ticketPrices.stream()
                        .collect(Collectors.toMap(
                                EventTicketPrice::getId,
                                Function.identity()
                        ));


        for ( BookingItemRequest request : requestDTO.items()){
            EventTicketPrice ticketPrice = priceLookup.get(request.eventTicketPriceId());

            if (event.getStatus() != EventStatus.PUBLISHED){
                throw  new RuntimeException("Event is not available for booking");
            }

            if (ticketPrice == null){
                throw new RuntimeException("Invalid ticket type");
            }

           if (request.quantity() > ticketPrice.getRemainingTickets()){
               throw new RuntimeException("Insufficient slots remaining" + ticketPrice.getRemainingTickets() + "remaining");
           }
        }


    }

}
