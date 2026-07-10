package com.tiko.tiko.Events.Service;

import com.tiko.tiko.Events.DTO.EventCategoryResponseDTO;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Repository.EventCategoryRepo;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Events.Repository.TicketTypesRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
}
