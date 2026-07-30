package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Users.Entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookingFactory {

    public Booking create(
            CreateBookingRequestDTO dto,
            Event event,
            User user,
            String bookingRef,
            Map<Long, EventTicketPrice> ticketPrices
    ) {

        Booking booking = new Booking(
                bookingRef,
                0L,
                event,
                user
        );

        List<BookingItem> items = createBookingItems(dto, ticketPrices, booking);

        booking.setBookingItems(items);
        booking.setTotalAmount(getTotalAmount(items));

        return booking;
    }

    private List<BookingItem> createBookingItems(
            CreateBookingRequestDTO dto,
            Map<Long, EventTicketPrice> ticketPrices,
            Booking booking) {

        List<BookingItem> items = new ArrayList<>();

        for (BookingItemRequest itemRequest : dto.items()) {

            EventTicketPrice ticketPrice =
                    ticketPrices.get(itemRequest.eventTicketPriceId());

            items.add(new BookingItem(
                    itemRequest.quantity(),
                    ticketPrice.getPrice(),
                    ticketPrice.getTicketType().getName(),
                    booking,
                    ticketPrice
            ));
        }

        return items;
    }

    private Long getTotalAmount(List<BookingItem> items) {
        return items.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
}
