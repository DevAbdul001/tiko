package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingReservationTest {

    @InjectMocks
    private BookingReservationService reservationService;

    @Mock
    private EventTicketPriceRepository repository;

    @Test
    void shouldReserveTickets(){
        BookingItem item = new BookingItem();
        item.setQuantity(2);

        EventTicketPrice price = new EventTicketPrice();
        price.setId(1L);
        price.setRemainingTickets(2);

        item.setEventTicketPrice(price);

        when(repository.findByIdForUpdate(1L)).thenReturn(Optional.of(price));


        reservationService.reserveTickets(List.of(item));
    }

    @Test
    void shouldThrowErrorForInsufficientSLots(){
        BookingItem item = new BookingItem();
        item.setQuantity(2);

        EventTicketPrice price = new EventTicketPrice();
        price.setId(1L);
        price.setRemainingTickets(0);

        item.setEventTicketPrice(price);

        when(repository.findByIdForUpdate(1L)).thenReturn(Optional.of(price));

        assertThrows(
                RuntimeException.class,
                () -> reservationService.reserveTickets(List.of(item))
        );

    }
}
