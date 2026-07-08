package com.tiko.tiko.Events.Controller;

import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Events.DTO.CreateEventRequestDTO;
import com.tiko.tiko.Events.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventService eventService;
    private final JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(
            @RequestBody CreateEventRequestDTO dto
            ){}
}
