package com.tiko.tiko.Booking.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateBookingRequestDTO(
        Long eventId,
        String idempotencyKey,
        @NotEmpty
        List<@Valid BookingItemRequest> items
) {
        public int getTotalTickets(){
                return items.stream()
                        .mapToInt(BookingItemRequest::quantity)
                        .sum();
        }

        public Map<Long, Integer> ticketQuantities(){
                return  items.stream()
                        .collect(Collectors.toMap(
                                BookingItemRequest::eventTicketPriceId,
                                BookingItemRequest::quantity,
                                Integer::sum
                        ));
        }

        public Set<Long> ticketPriceIds(){
                return items.stream()
                        .map(BookingItemRequest::eventTicketPriceId)
                        .collect(Collectors.toSet());
        }

}
