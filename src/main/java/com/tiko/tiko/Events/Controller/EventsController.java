package com.tiko.tiko.Events.Controller;

import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Events.DTO.*;
import com.tiko.tiko.Events.Service.EventService;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventService eventService;
    private final JWTService jwtService;
    private final UserService userService;

    @GetMapping("/categories")
    public List<EventCategoryResponseDTO> fetchEventCategories(){
       return eventService.fetchEventCategories();
    }

    @GetMapping("/ticketTypes")
    public List<TicketTypeResponseDTO> fetchAllTicketsTypes (){
        return eventService.fetchAllTicketTypes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDetailsDTO createEvent(
            @RequestBody CreateEventRequestDTO dto,
            @CookieValue("accessToken") String accessToken
            ){
        Long userId = jwtService.extractUserId(accessToken);
        User user = userService.getUserById(userId);

        return eventService.createEvent(dto, user);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @RequestParam(defaultValue = "0" ) int page
    ) {
        Page<EventResponseDTO> events = eventService.getEvents(page);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public EventDetailsResponseDTO getEventByid(
            @RequestParam("eventId") Long eventId
    ){
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/update")
    public EventDetailsResponseDTO updateEvent(
            @RequestBody Long eventId, UpdateEventRequest request
    ){
       eventService.updateEvent(eventId, request);
       return eventService.getEventById(eventId);
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @CookieValue("accessToken") String accessToken
    ) {
        Long userId = jwtService.extractUserId(accessToken);

        eventService.deleteEvent(userId, eventId);

        return ResponseEntity.noContent().build();
    }

}
