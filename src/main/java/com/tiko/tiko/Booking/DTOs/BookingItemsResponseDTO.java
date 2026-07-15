package com.tiko.tiko.Booking.DTOs;

public record BookingItemsResponseDTO(
        Long id,
        Long bookingId,
        String ticketName, //VVIP. VIP, Regular etc
        int quantity,
        Long unit_price
) {
}
