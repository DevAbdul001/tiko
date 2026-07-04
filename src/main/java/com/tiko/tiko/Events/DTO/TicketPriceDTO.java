package com.tiko.tiko.Events.DTO;

public record TicketPriceDTO(
        Long id,
        Long eventId,
        String ticketType,
        int price,
        int quantity
) {}
