package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.BookingResponseDTO;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.DTOs.IdempotencyRecord;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import com.tiko.tiko.Booking.Repository.BookingItemsRepository;
import com.tiko.tiko.Booking.Repository.BookingRepository;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import com.tiko.tiko.Users.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingItemsRepository bookingItemsRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private EventTicketPriceRepository eventTicketPriceRepository;

    @Autowired
    private IdempotencyService idempotencyService;


    //utility method : generates a random 36 char string
    private String generateBookingRef(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[27];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    //Entity lookups
    private User getUser(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User does not exist"));
    }

    private Event getEvent(Long eventId){
        return eventsRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    //Validation
    private int validateTicketAvailability(Long eventId){
        Optional<Event> event = eventsRepo.findById(eventId);
        return event.get().getCapacity();
    }

    private int validateTicketTypeAvailability(Long eventTicketPriceId){
        Optional<EventTicketPrice> eventTicketPrice = eventTicketPriceRepository.findById(eventTicketPriceId);
        return eventTicketPrice.get().getQuantity();
    }






}
