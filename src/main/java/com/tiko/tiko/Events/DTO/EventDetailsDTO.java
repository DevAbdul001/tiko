package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Enums.EventStatus;

import java.time.LocalDateTime;

public record EventDetailsDTO(
        Long id,
        String name,
        LocalDateTime date,
        String location,
        int capacity,
        String description,
        EventStatus status,
        String imageUrl,

        String organizerName,
        String organizerEmail
) {}
