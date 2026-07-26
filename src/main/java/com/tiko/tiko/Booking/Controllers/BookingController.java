package com.tiko.tiko.Booking.Controllers;


import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Booking.DTOs.*;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{bookingId}")
    public BookingDetailsResponseDTO getBookingById(
            @RequestParam("bookingId") Long bookingId,
            @CookieValue("accessToken") String token
    ){
        Long userId = jwtService.extractUserId(token);
        return bookingService.fetchBookingById(bookingId, userId);
    }

    @GetMapping
    public Page<BookingResponseDTO>  getUserBookingSummary(
            @CookieValue("accessToken") String token,
            @RequestParam(defaultValue = "0" ) int page
    ){
        Long userId = jwtService.extractUserId(token);
        return bookingService.getBookingSummaryForUser(userId, page);
    }

    @PatchMapping("/{bookingId}")
    public List<BookingItemsResponseDTO> updateBooking(
            @RequestBody UpdateBookingDTO dto,
            @CookieValue("accessToken") String token
    ){
        Long userId = jwtService.extractUserId(token);
        return bookingService.updateBookingItems(dto, userId);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(
            @RequestParam("bookingId") Long bookingId,
            @CookieValue("accessToken") String token
    ){
        bookingService.deleteBooking(bookingId, jwtService.extractUserId(token));
    }

}
