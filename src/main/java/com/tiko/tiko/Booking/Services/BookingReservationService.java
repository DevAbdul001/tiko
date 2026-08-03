package com.tiko.tiko.Booking.Services;


import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookingReservationService {

    @Autowired
    private EventTicketPriceRepository repository;

    public void reserveTickets(
            List<BookingItem> bookingItems
    ){

        for (BookingItem item : bookingItems){
            EventTicketPrice ticketPrice = item.getEventTicketPrice();
            EventTicketPrice ticketForUpdate = repository.findByIdForUpdate(ticketPrice.getId())
                    .orElseThrow(()-> new RuntimeException(
                            "Ticket type not found"
                    ));

            if (ticketForUpdate.getRemainingTickets() < item.getQuantity()){
                throw new RuntimeException(
                        "Insufficient slots remaining. Available:"
                                + ticketPrice.getRemainingTickets() );
            }

            ticketForUpdate.setRemainingTickets(ticketForUpdate.getRemainingTickets()- item.getQuantity());
        }
    }
}
