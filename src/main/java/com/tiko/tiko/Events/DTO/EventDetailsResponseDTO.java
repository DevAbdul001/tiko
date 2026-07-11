package com.tiko.tiko.Events.DTO;

import java.util.List;

public record EventDetailsResponseDTO(
        EventDetailsDTO dto,
        List<TicketPriceDTO> ticketPrices
) {}
