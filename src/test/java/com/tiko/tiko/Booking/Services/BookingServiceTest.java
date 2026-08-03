package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.BookingItemRequest;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Repository.BookingItemsRepository;
import com.tiko.tiko.Booking.Repository.BookingRepository;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.TestDataFactory.EventTicketPriceFactory;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingScenario;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingScenarioFactory;
import com.tiko.tiko.TestDataFactory.Scenerios.EventScenario;
import com.tiko.tiko.TestDataFactory.Scenerios.EventScenarioFactory;
import com.tiko.tiko.TestDataFactory.TicketTypeFactory;
import com.tiko.tiko.TestDataFactory.UserFactory;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingItemsRepository bookingItemsRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private IdempotencyService idempotencyService;


    @Mock
    private BookingValidationService validationService;

    @Mock
    private BookingFactory bookingFactory;

    @Mock
    private BookingReservationService reservationService;

    @Mock
    private BookingContextLoader contextLoader;

    @Mock
    private IdempotencyExecutor idempotencyExecutor;

    @Mock
    private TestEntityManager em;


    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldCreateBooking(){
        User user = UserFactory.create();

        EventScenario eventScenario = EventScenarioFactory.create(em);
        Event event = eventScenario.event();
        event.setStatus(EventStatus.PUBLISHED);

        TicketType ticketType = TicketTypeFactory.create();
        EventTicketPrice eventTicketPrice = EventTicketPriceFactory.create(event, ticketType);
        eventTicketPrice.setRemainingTickets(5);
        eventTicketPrice.setPrice(300L);

        BookingScenario bookingScenario = BookingScenarioFactory.create(em);
        Booking booking = bookingScenario.booking();

        BookingItemRequest itemRequest = new BookingItemRequest(1L, 3);
        CreateBookingRequestDTO requestDTO = new CreateBookingRequestDTO(
                2L, "idem-key", List.of(itemRequest) );

        BookingContext context = new BookingContext(
               event,
                Map.of(1L, eventTicketPrice)
        );

        when(userRepo.findById(3L)).thenReturn(Optional.of(user));
        when(contextLoader.load(2L)).thenReturn(context);
        when(bookingFactory.create(requestDTO, context, user)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(idempotencyService.get("idem-key"))
                .thenReturn(null);

        when(idempotencyExecutor.execute(anyString(), any()))
                .thenAnswer(invocation -> {
                    Supplier<Booking> supplier = invocation.getArgument(1);
                    return supplier.get();
                });

        Booking result = bookingService.createBooking(requestDTO, 3L);

        assertNotNull(result);
    }

}
