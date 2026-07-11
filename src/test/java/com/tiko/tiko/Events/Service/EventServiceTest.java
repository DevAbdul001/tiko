package com.tiko.tiko.Events.Service;

import com.tiko.tiko.Events.DTO.*;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Events.Repository.EventCategoryRepo;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Events.Repository.TicketTypesRepo;
import com.tiko.tiko.Users.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventsRepo eventsRepo;

    @Mock
    private EventCategoryRepo eventCategoryRepo;

    @Mock
    private TicketTypesRepo ticketTypesRepo;

    @Mock
    private EventTicketPriceRepository eventTicketPriceRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void fetchEventCategories() {
        // Arrange
        List<EventCategory> categories = List.of(
                new EventCategory( "Hackathon"),
                new EventCategory("Conference"),
                new EventCategory( "Lecture")
        );

        when(eventCategoryRepo.findAll()).thenReturn(categories);

        // Act
        List<EventCategoryResponseDTO> result = eventService.fetchEventCategories();

        // Assert
        assertEquals(3, result.size());

        assertEquals("Hackathon", result.get(0).name());
        assertEquals("Conference", result.get(1).name());
        assertEquals("Lecture", result.get(2).name());
    }

    @Test
    void shouldFetchAllTicketTypes(){
        List<TicketType> types = List.of(
                new TicketType("VVIP"),
                new TicketType("VIP"),
                new TicketType("Regular")
        );

        when(ticketTypesRepo.findAll()).thenReturn(types);

        List<TicketTypeResponseDTO> result = eventService.fetchAllTicketTypes();

        assertEquals(3, result.size());

        assertEquals("VVIP", result.get(0).name());
        assertEquals("VIP", result.get(1).name());
        assertEquals("Regular", result.get(2).name());
    }

    @Test
    void shouldCreateEvent() {
        // Arrange
        List<TicketPriceRequestDTO> ticketPriceRequestDTOList = List.of(
                new TicketPriceRequestDTO(1L, 500, 200),
                new TicketPriceRequestDTO(2L, 300, 200),
                new TicketPriceRequestDTO(3L, 150, 100)
        );

        User organizer = new User(
                "Doe",
                "doe@app.com",
                "password"
        );

        EventCategory category = new EventCategory("Hackathon");
        category.setId(1L);

        TicketType vip = new TicketType();
        vip.setId(1L);

        TicketType regular = new TicketType();
        regular.setId(2L);

        TicketType student = new TicketType();
        student.setId(3L);

        when(eventCategoryRepo.findById(1L))
                .thenReturn(Optional.of(category));

        when(ticketTypesRepo.findById(1L))
                .thenReturn(Optional.of(vip));

        when(ticketTypesRepo.findById(2L))
                .thenReturn(Optional.of(regular));

        when(ticketTypesRepo.findById(3L))
                .thenReturn(Optional.of(student));

        when(eventsRepo.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateEventRequestDTO dto = new CreateEventRequestDTO(
                "Java Conference",
                LocalDateTime.now(),
                500L,
                "Mombasa",
                "Description",
                1L,
                ticketPriceRequestDTOList
        );

        // Act
        EventDetailsDTO result = eventService.createEvent(dto, organizer);

        // Assert
        assertEquals("Doe", result.organizerName());

        verify(eventCategoryRepo).findById(1L);
        verify(ticketTypesRepo).findById(1L);
        verify(ticketTypesRepo).findById(2L);
        verify(ticketTypesRepo).findById(3L);
        verify(eventsRepo).save(any(Event.class));
    }

}
