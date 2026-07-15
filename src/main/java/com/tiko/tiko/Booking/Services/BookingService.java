package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.BookingResponseDTO;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Booking.Repository.BookingItemsRepository;
import com.tiko.tiko.Booking.Repository.BookingRepository;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
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

    //utility method : generates a random 36 char string
    private String generateBookingRef(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[27];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public void createBooking(Long userId, CreateBookingRequestDTO request){
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User does not exist"));
        String bookingRef = this.generateBookingRef();

        Event event = eventsRepo.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Long> eventTicketPriceIds = request.items()
                .stream()
                .map(BookingItemRequest::eventTicketPriceId)
                .toList();


        List<EventTicketPrice> eventTicketPrices = eventTicketPriceRepository.findAllById(eventTicketPriceIds);

        if (eventTicketPrices.size() != eventTicketPriceIds.size()) {
            throw new RuntimeException("One or more ticket prices do not exist.");
        }

        Map<Long, EventTicketPrice> ticketPriceMap = new HashMap<>();

        for (EventTicketPrice ticketPrice : eventTicketPrices ) {
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

            if(ticketPrice == null) {
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

    }


}
