package com.tiko.tiko.TestDataFactory.dtos;

import com.tiko.tiko.Events.DTO.TicketPriceRequestDTO;

public final class TicketPriceRequestFactory {

    private TicketPriceRequestFactory() {
    }

    public static TicketPriceRequestDTO create(Long ticketTypeId) {
        return new TicketPriceRequestDTO(
                ticketTypeId,
                5000L,
                100
        );
    }

    public static TicketPriceRequestDTO create(
            Long ticketTypeId,
            Long price,
            int quantity) {

        return new TicketPriceRequestDTO(
                ticketTypeId,
                price,
                quantity
        );
    }
}
