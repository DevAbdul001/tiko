package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Enums.EventStatus;

import java.time.LocalDateTime;

public record EventDetailsDTO(
        Long id,
        String organizerName,
        String organizerEmail,
        String eventName,
        LocalDateTime date,
        String location,
        int capacity,
        String imageUrl  ,
        EventStatus status

) {}
