package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Users.Entity.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class BookingFactory {

    public Booking create(
            CreateBookingRequestDTO requestDTO,
            BookingContext context,
            User user
    ){
        long totalAmount = 0L;
        List<BookingItem> bookingItems = new ArrayList<>();
        Event event = context.event();
        Map<Long, EventTicketPrice> priceLookup = context.priceLookUp();
        Booking booking = new Booking(
                generateBookingRef(),
                0L,
                event,
                user
        );

        for (BookingItemRequest requestItem : requestDTO.items()){
            EventTicketPrice ticketPrice = priceLookup.get(requestItem.eventTicketPriceId());
            BookingItem item = new BookingItem(
                    requestItem.quantity(),
                    ticketPrice.getPrice(),
                    ticketPrice.getTicketType().getName(), //TODO : Create a lookup for ticketTypes
                    booking,
                    ticketPrice
            );
            long amount = ticketPrice.getPrice() * requestItem.quantity();
            totalAmount += amount;
            bookingItems.add(item);
        }

        booking.setTotalAmount(totalAmount);
        bookingItems.forEach(booking::addBookingItem);
        return booking;

        }

    private String generateBookingRef(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[27];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
