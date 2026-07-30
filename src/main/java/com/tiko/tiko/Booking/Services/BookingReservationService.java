package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class BookingReservationService {

    @Autowired
    private EventTicketPriceRepository eventTicketPriceRepository;



    public void reserveTicket(Long ticketPriceId, int quantity){

        Optional<EventTicketPrice> ticket = eventTicketPriceRepository.findByIdForUpdate(ticketPriceId);

        if (ticket.get().getRemainingTickets() < quantity) {
            throw  new RuntimeException("Not enough tickets" + ticket.get().getRemainingTickets());
        }
        ticket.get().setRemainingTickets(ticket.get().getRemainingTickets() - quantity);
    }
}
