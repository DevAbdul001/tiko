package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.IdempotencyRecord;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class IdempotencyExecutor {

    private final IdempotencyService idempotencyService;

    public IdempotencyExecutor(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    public Booking execute(
            String key,
            Supplier<Booking> action
    ) {

        boolean started = idempotencyService.start(key);

        if (!started) {
            throw new RuntimeException("Duplicate request.");
        }

        try {

            Booking booking = action.get();

            idempotencyService.complete(key, booking);

            return booking;

        } catch (Exception e) {

            idempotencyService.remove(key);

            throw e;
        }
    }
}