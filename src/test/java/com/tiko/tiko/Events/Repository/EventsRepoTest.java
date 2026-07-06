package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.DTO.EventResponseDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class EventsRepoTest {
    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventCategoryRepo eventCategoryRepo;

    @Test
    void shouldFetchEvents(){

        User user = new User(
                "Doe", "doe@app.com","password"
        );
        User organizer = userRepo.save(user);
        System.out.println(organizer);

        EventCategory category = new EventCategory("Hackathon");
        eventCategoryRepo.save(category);

        Event event = new Event(
                "Java Conference",
                organizer,
                LocalDateTime.now(),
                category,
                500L,
                "Nairobi"
        );
        event.setImageUrl("image.png");
        eventsRepo.save(event);
        Pageable pageable = PageRequest.of(0, 20);
        Page<EventResponseDTO> response = eventsRepo.findAllEvents(pageable);

        assertNotNull(response);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());

        EventResponseDTO dto = response.getContent().get(0);

        assertEquals(event.getId(), dto.id());
        assertEquals("Doe", dto.organizerName());
        assertEquals("Java Conference", dto.name());
        assertEquals("Nairobi", dto.location());
        assertEquals("image.png", dto.imageUrl());
        assertEquals(event.getStatus(), dto.status());
    }
}
