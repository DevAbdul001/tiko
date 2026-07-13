package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Enums.EventStatus;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        EventStatus status,
        String location,
        LocalDateTime date,
        Long capacity,
        String imageUrl
) {}
