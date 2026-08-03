package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Enums.EventStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookingValidationService {


    public void validate(
            CreateBookingRequestDTO requestDTO,
            BookingContext context
    ){

        if (requestDTO.items().isEmpty()) {
            throw new RuntimeException("Booking must contain at least one ticket");
        }


        Event event = context.event();

        Map<Long, EventTicketPrice> priceLookup = context.priceLookUp();

        if (event.getStatus() != EventStatus.PUBLISHED){
            throw  new RuntimeException("Event is not available for booking");
        }


        for ( BookingItemRequest request : requestDTO.items()){
            EventTicketPrice ticketPrice = priceLookup.get(request.eventTicketPriceId());


            if (ticketPrice == null){
                throw new RuntimeException("Invalid ticket type");
            }

           if (request.quantity() > ticketPrice.getRemainingTickets()){
               throw new RuntimeException(
                       "Insufficient slots remaining. Available: "
                               + ticketPrice.getRemainingTickets()
               );
           }

        }


    }

}
