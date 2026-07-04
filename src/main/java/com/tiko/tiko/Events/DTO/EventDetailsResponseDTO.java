package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Utils.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record EventDetailsResponseDTO(
        Long id,
        String name,
        String description,
        LocalDateTime date,
        String location,
        String capacity,
        EventStatus status,
        String imageUrl,

        String organizerName,
        String organizerEmail,

        List<TicketPriceDTO> ticketPrices
) {
}
