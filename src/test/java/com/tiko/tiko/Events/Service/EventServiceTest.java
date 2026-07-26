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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.tiko.tiko.Events.Enums.EventStatus.PUBLISHED;
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
                new TicketPriceRequestDTO(1L, 500L, 200),
                new TicketPriceRequestDTO(2L, 300L, 200),
                new TicketPriceRequestDTO(3L, 150L, 100)
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
                500,
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

    @Test
    void shouldGetEvents(){
        Pageable pageable = PageRequest.of(0, 20);

        EventResponseDTO event = new EventResponseDTO(
                1L,
                "John Doe",
                "Java Hackathon",
                LocalDateTime.now(),
                "Mombasa",
                "image.png",
                PUBLISHED
        );

        Page<EventResponseDTO> page = new PageImpl<>(List.of(event));

        when(eventsRepo.findAllEvents(any(Pageable.class)))
                .thenReturn(page);

        Page<EventResponseDTO> result = eventService.getEvents(0);

        assertEquals(1, result.getTotalElements());
        assertEquals("Java Hackathon", result.getContent().get(0).name());

        verify(eventsRepo).findAllEvents(pageable);
    }

    @Test
    void shouldFetchEventById(){
        User organizer = new User( "Doe", "doe@email.com", "password");
        EventCategory eventCategory = new EventCategory("Hackathon");
        EventDetailsDTO eventDetailsDTO = new EventDetailsDTO(
                1L,

                "Doe",
                "Doe@mail.com",
                "Java hackathon",
                LocalDateTime.now(),
                "Mombasa",
                5000,
                "image.png",
                PUBLISHED

        );
        List<TicketPriceDTO> ticketPriceDTOList =  List.of(
                new TicketPriceDTO(
                        1L,
                        "VIP",
                        500L,
                        300
                ),
                new TicketPriceDTO(
                        2L,
                        "Regular",
                        300L,
                        200
                ),
                new TicketPriceDTO(
                        3L,
                        "VVIP",
                        1000L,
                        100
                )
        );

        when(eventsRepo.findById(1L))
                .thenReturn(
                        Optional.of(new Event(
                                "Java hackathon",
                                organizer,
                                LocalDateTime.now(),
                                eventCategory,
                                5000,
                                "Mombasa"

                        ))
                );

        when(eventsRepo.findByEventId(1L))
                .thenReturn( eventDetailsDTO );

        when(eventTicketPriceRepository.fetchEventTicketPrices(1L))
                .thenReturn( ticketPriceDTOList );

        EventDetailsResponseDTO result = eventService.getEventById(1L);

        assertEquals("Doe", result.dto().organizerName());

    }

    @Test
    void shouldUpdateEvent(){
        User organizer = new User( "Doe", "doe@email.com", "password");
        EventCategory eventCategory = new EventCategory("Hackathon");
        Event event = new Event(
                "Java hackathon",
                organizer,
                LocalDateTime.now(),
                eventCategory,
                5000,
                "Mombasa"

        );
        UpdateEventRequest request = new UpdateEventRequest(
                PUBLISHED,
                "Nairobi",
                LocalDateTime.now(),
                7000,
                "image.png"
        );

        when(eventsRepo.findById(1L))
                .thenReturn(
                        Optional.of(event)
                );

        eventService.updateEvent(1L, request, organizer.getId());

        verify(eventsRepo).save(any(Event.class));
    }

    @Test
    void shouldDeleteEvent(){
        User organizer = new User( "Doe", "doe@email.com", "password");
        organizer.setId(1L);
        EventCategory eventCategory = new EventCategory("Hackathon");
        Event event = new Event(
                "Java hackathon",
                organizer,
                LocalDateTime.now(),
                eventCategory,
                5000,
                "Mombasa"

        );

        when(eventsRepo.findById(2L))
                .thenReturn(Optional.of(event));

        eventService.deleteEvent(1L, 2L);

        verify(eventsRepo).deleteById(any(Long.class));
    }

}
