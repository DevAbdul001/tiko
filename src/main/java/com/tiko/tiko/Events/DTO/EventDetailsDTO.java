package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Utils.EventStatus;

import java.time.LocalDateTime;

public record EventDetailsDTO(
        Long id,
        String name,
        LocalDateTime date,
        String location,
        String capacity,
        EventStatus status,
        String imageUrl,

        String organizerName,
        String organizerEmail
) {}
