package com.tiko.tiko.Booking.Controllers;


import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Booking.DTOs.CreateBookingRequestDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Services.BookingService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(
            @RequestBody CreateBookingRequestDTO dto,
            @CookieValue("accessToken") String token
            ) {
        Long userId = jwtService.extractUserId(token);
        return bookingService.createBooking(dto, userId);
    }

}
