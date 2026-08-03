package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Entities.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdempotencyExecutorTest {

    @InjectMocks
    private IdempotencyExecutor executor;

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private Supplier<Booking> action;

    @Test
    void shouldExecuteActionSuccessfully() {

        Booking booking = new Booking();

        when(idempotencyService.start("idem-key")).thenReturn(true);
        when(action.get()).thenReturn(booking);

        Booking result = executor.execute("idem-key", action);

        assertNotNull(result);
        assertEquals(booking, result);

        verify(idempotencyService).start("idem-key");
        verify(action).get();
        verify(idempotencyService).complete("idem-key", booking);
        verify(idempotencyService, never()).remove(anyString());
    }

    @Test
    void shouldThrowWhenDuplicateRequest() {

        when(idempotencyService.start("idem-key")).thenReturn(false);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> executor.execute("idem-key", action)
        );

        assertEquals("Duplicate request.", ex.getMessage());

        verify(action, never()).get();
        verify(idempotencyService, never()).complete(anyString(), any());
        verify(idempotencyService, never()).remove(anyString());
    }

    @Test
    void shouldRemoveKeyWhenActionFails() {

        when(idempotencyService.start("idem-key")).thenReturn(true);
        when(action.get()).thenThrow(new RuntimeException("Something went wrong"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> executor.execute("idem-key", action)
        );

        assertEquals("Something went wrong", ex.getMessage());

        verify(idempotencyService).remove("idem-key");
        verify(idempotencyService, never()).complete(anyString(), any());
    }
}