package com.tiko.tiko.Events.DTO;

public record TicketPriceDTO(
        Long id,
        String ticketType,
        int price,
        int quantity
) {}
