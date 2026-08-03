package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Enums.EventStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingValidationServiceTest {

    private BookingValidationService validationService;

    @Mock
    private BookingContext context;

    @BeforeEach
    void setUp() {
        validationService = new BookingValidationService();
    }

    @Test
    void shouldThrowWhenBookingHasNoItems() {

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(1L, "idem-key", List.of());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> validationService.validate(request, context)
        );

        assertEquals("Booking must contain at least one ticket", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEventIsNotPublished() {

        Event event = new Event();
        event.setStatus(EventStatus.DRAFT);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of());

        BookingItemRequest item = new BookingItemRequest(1L, 1);

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(item)
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> validationService.validate(request, context)
        );

        assertEquals("Event is not available for booking", ex.getMessage());
    }

    @Test
    void shouldThrowWhenTicketTypeIsInvalid() {

        Event event = new Event();
        event.setStatus(EventStatus.PUBLISHED);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of());

        BookingItemRequest item = new BookingItemRequest(99L, 1);

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(item)
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> validationService.validate(request, context)
        );

        assertEquals("Invalid ticket type", ex.getMessage());
    }

    @Test
    void shouldThrowWhenInsufficientTicketsRemain() {

        Event event = new Event();
        event.setStatus(EventStatus.PUBLISHED);

        EventTicketPrice price = new EventTicketPrice();
        price.setRemainingTickets(5);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of(1L, price));

        BookingItemRequest item = new BookingItemRequest(1L, 10);

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(item)
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> validationService.validate(request, context)
        );

        assertEquals(
                "Insufficient slots remaining. Available: 5",
                ex.getMessage()
        );
    }

    @Test
    void shouldValidateSuccessfully() {

        Event event = new Event();
        event.setStatus(EventStatus.PUBLISHED);

        EventTicketPrice price = new EventTicketPrice();
        price.setRemainingTickets(20);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of(1L, price));

        BookingItemRequest item = new BookingItemRequest(1L, 2);

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(item)
                );

        assertDoesNotThrow(
                () -> validationService.validate(request, context)
        );
    }

    @Test
    void shouldValidateMultipleTicketTypesSuccessfully() {

        Event event = new Event();
        event.setStatus(EventStatus.PUBLISHED);

        EventTicketPrice vip = new EventTicketPrice();
        vip.setRemainingTickets(10);

        EventTicketPrice regular = new EventTicketPrice();
        regular.setRemainingTickets(100);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(
                Map.of(
                        1L, vip,
                        2L, regular
                )
        );

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(
                                new BookingItemRequest(1L, 2),
                                new BookingItemRequest(2L, 5)
                        )
                );

        assertDoesNotThrow(
                () -> validationService.validate(request, context)
        );
    }

    @Test
    void shouldThrowWhenOneOfMultipleTicketTypesIsInvalid() {

        Event event = new Event();
        event.setStatus(EventStatus.PUBLISHED);

        EventTicketPrice vip = new EventTicketPrice();
        vip.setRemainingTickets(10);

        when(context.event()).thenReturn(event);
        when(context.priceLookUp()).thenReturn(Map.of(1L, vip));

        CreateBookingRequestDTO request =
                new CreateBookingRequestDTO(
                        1L,
                        "idem-key",
                        List.of(
                                new BookingItemRequest(1L, 2),
                                new BookingItemRequest(999L, 1)
                        )
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> validationService.validate(request, context)
        );

        assertEquals("Invalid ticket type", ex.getMessage());
    }
}