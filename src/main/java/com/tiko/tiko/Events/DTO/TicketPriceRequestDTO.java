package com.tiko.tiko.Events.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


public record TicketPriceRequestDTO(

        @NotNull
        Long ticketTypeId,

        @PositiveOrZero
        int price,

        @Positive
        int quantity

) {}
