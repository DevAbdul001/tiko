package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Enums.EventStatus;

import java.time.LocalDateTime;

public record EventResponseDTO(
        Long id,
        String organizerName,
        String name,
        LocalDateTime date,
        String location,
        String imageUrl,
        EventStatus status
) {}
