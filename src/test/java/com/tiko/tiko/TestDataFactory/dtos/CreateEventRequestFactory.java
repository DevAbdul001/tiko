package com.tiko.tiko.TestDataFactory.dtos;

import com.tiko.tiko.Events.DTO.CreateEventRequestDTO;
import com.tiko.tiko.Events.DTO.TicketPriceRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public final class CreateEventRequestFactory {

    private CreateEventRequestFactory() {
    }

    public static CreateEventRequestDTO create(
            Long categoryId,
            Long ticketTypeId) {

        return new CreateEventRequestDTO(
                "Test Event",
                LocalDateTime.now().plusDays(30),
                100,
                "Nairobi",
                "A test event",
                categoryId,
                List.of(
                        TicketPriceRequestFactory.create(ticketTypeId)
                )
        );
    }

    public static CreateEventRequestDTO create(
            String name,
            LocalDateTime date,
            int capacity,
            String location,
            String description,
            Long categoryId,
            List<TicketPriceRequestDTO> tickets) {

        return new CreateEventRequestDTO(
                name,
                date,
                capacity,
                location,
                description,
                categoryId,
                tickets
        );
    }
}