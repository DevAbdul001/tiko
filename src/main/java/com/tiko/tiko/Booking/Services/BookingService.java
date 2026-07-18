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

    private User getUser(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User does not exist"));
    }

    private Event getEvent(Long eventId){
        return eventsRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }


    public Booking createBooking(Long userId, CreateBookingRequestDTO request){

        String bookingRef = this.generateBookingRef();
        User user = this.getUser(userId);
        Event event = this.getEvent(request.eventId());


        IdempotencyRecord existing  = idempotencyService.get(request.idempotencyKey());

        if( existing != null ){
            if (existing.status() == IdempotencyStatus.PROCESSING) {
                throw new RuntimeException("This request is already being processed");
            }

            if (existing.status() == IdempotencyStatus.COMPLETED){
                return bookingRepository.findById(existing.bookingId())
                        .orElseThrow(() ->
                                new RuntimeException("Booking record not found."));
            }
        }

        boolean acquired = idempotencyService.start(request.idempotencyKey());

        if (!acquired) {
            IdempotencyRecord record = idempotencyService.get(request.idempotencyKey());
            if (record.status() == IdempotencyStatus.COMPLETED) {

                return bookingRepository.findById(record.bookingId())
                        .orElseThrow(() ->
                                new RuntimeException("Booking not found"));
            }

            throw new RuntimeException("Request already in progress.");
        }

        try {

            List<Long> eventTicketPriceIds = request.items()
                    .stream()
                    .map(BookingItemRequest::eventTicketPriceId)
                    .toList();


            List<EventTicketPrice> eventTicketPrices = eventTicketPriceRepository.findAllById(eventTicketPriceIds);

            if (eventTicketPrices.size() != eventTicketPriceIds.size()) {
                throw new RuntimeException("One or more ticket prices do not exist.");
            }

            Map<Long, EventTicketPrice> ticketPriceMap = new HashMap<>();

            for (EventTicketPrice ticketPrice : eventTicketPrices) {
                ticketPriceMap.put(ticketPrice.getId(), ticketPrice);

                if (!ticketPrice.getEvent().getId().equals(event.getId())) {
                    throw new RuntimeException("Ticket does not belong to this event.");
                }
            }


            Long totalAmount = 0L;
            List<BookingItem> bookingItems = new ArrayList<>();


            for (BookingItemRequest bookingItemRequest : request.items()) {
                EventTicketPrice ticketPrice =
                        ticketPriceMap.get(bookingItemRequest.eventTicketPriceId());

                if (ticketPrice == null) {
                    throw new RuntimeException("Ticket price not found:" + bookingItemRequest.eventTicketPriceId());
                }

                Long subTotal = ticketPrice.getPrice() * bookingItemRequest.quantity();

                totalAmount += subTotal;

                BookingItem bookingItem = new BookingItem(
                        bookingItemRequest.quantity(),
                        ticketPrice.getPrice(),
                        ticketPrice.getTicketType().getName(),
                        null,
                        ticketPrice
                );
                bookingItems.add(bookingItem);

            }

            Booking booking = new Booking(
                    bookingRef,
                    totalAmount,
                    event,
                    user
            );

            for (BookingItem item : bookingItems) {
                booking.addBookingItem(item);
            }
            bookingRepository.save(booking);
            idempotencyService.complete(request.idempotencyKey(), booking);
            return booking;
        } catch (Exception e) {
            idempotencyService.remove(request.idempotencyKey());
            throw new RuntimeException(e);
        }

    }


}
