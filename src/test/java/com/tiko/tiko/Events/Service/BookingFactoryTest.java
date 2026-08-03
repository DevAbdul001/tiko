package com.tiko.tiko.Events.Service;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Services.BookingFactory;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Users.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingFactoryTest {

    private final BookingFactory factory = new BookingFactory();

    @Mock
    private BookingContext context;

    @Test
    void shouldCreateBookingEntity() {

        // arrange
        Event event = new Event();
        User user = new User();

        TicketType ticketType = new TicketType();
        ticketType.setName("VIP");

        EventTicketPrice price = new EventTicketPrice();
        price.setPrice(5000L);
        price.setTicketType(ticketType);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of(1L, price));

        CreateBookingRequestDTO request = new CreateBookingRequestDTO(
                1L,
                "idem-key",
                List.of(new BookingItemRequest(1L, 2))
        );

        // act
        Booking booking = factory.create(request, context, user);

        // assert
        assertNotNull(booking);
        assertEquals(event, booking.getEvent());
        assertEquals(user, booking.getUser());
        assertEquals(10000L, booking.getTotalAmount());
        assertEquals(1, booking.getBookingItems().size());
        assertNotNull(booking.getBookingRef());

    }
}
