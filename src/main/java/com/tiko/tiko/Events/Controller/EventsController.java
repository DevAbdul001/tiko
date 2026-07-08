package com.tiko.tiko.Events.Controller;

import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Events.DTO.CreateEventRequestDTO;
import com.tiko.tiko.Events.DTO.EventCategoryResponseDTO;
import com.tiko.tiko.Events.DTO.TicketTypeResponseDTO;
import com.tiko.tiko.Events.Service.EventService;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public void createEvent(
            @RequestBody CreateEventRequestDTO dto,
            @CookieValue("accessToken") String accessToken
            ){
        Long userId = jwtService.extractUserId(accessToken);
        User user = userService.getUserById(userId);

        eventService.createEvent(dto, user);
    }
}
