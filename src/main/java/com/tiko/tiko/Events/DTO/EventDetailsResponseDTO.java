package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Utils.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record EventDetailsResponseDTO(
        EventDetailsDTO dto,
        List<TicketPriceDTO> ticketPrices
) {}
