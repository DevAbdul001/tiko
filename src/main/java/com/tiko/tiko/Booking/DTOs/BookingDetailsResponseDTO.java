package com.tiko.tiko.Booking.DTOs;

import java.util.List;

public record BookingDetailsResponseDTO(
        BookingResponseDTO bookingResponseDTO,
        List<BookingItemsResponseDTO> items
) {}
