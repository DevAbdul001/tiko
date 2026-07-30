package com.tiko.tiko.Booking.Service;

import com.tiko.tiko.Booking.Services.BookingService;
import com.tiko.tiko.Booking.Services.IdempotencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private IdempotencyService idempotencyService;

    @Test
    void shouldCreateBooking(){

    }
}
