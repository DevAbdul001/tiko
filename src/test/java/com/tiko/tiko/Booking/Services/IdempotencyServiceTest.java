package com.tiko.tiko.Booking.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiko.tiko.Booking.DTOs.IdempotencyRecord;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdempotencyServiceTest {

    @InjectMocks
    private IdempotencyService service;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    void shouldStartIdempotencyRecord() throws Exception {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(objectMapper.writeValueAsString(any(IdempotencyRecord.class)))
                .thenReturn("json");
        when(valueOperations.setIfAbsent(
                eq("idem-key"),
                eq("json"),
                any(Duration.class)))
                .thenReturn(true);

        boolean started = service.start("idem-key");

        assertTrue(started);

        verify(valueOperations).setIfAbsent(
                eq("idem-key"),
                eq("json"),
                any(Duration.class));
    }

    @Test
    void shouldReturnFalseWhenKeyAlreadyExists() throws Exception {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(objectMapper.writeValueAsString(any(IdempotencyRecord.class)))
                .thenReturn("json");
        when(valueOperations.setIfAbsent(
                anyString(),
                anyString(),
                any(Duration.class)))
                .thenReturn(false);

        boolean started = service.start("idem-key");

        assertFalse(started);
    }

    @Test
    void shouldGetRecord() throws Exception {

        IdempotencyRecord record = new IdempotencyRecord(
                "idem-key",
                IdempotencyStatus.PROCESSING,
                null,
                null
        );

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("idem-key"))
                .thenReturn("json");
        when(objectMapper.readValue("json", IdempotencyRecord.class))
                .thenReturn(record);

        IdempotencyRecord result = service.get("idem-key");

        assertNotNull(result);
        assertEquals("idem-key", result.key());
        assertEquals(IdempotencyStatus.PROCESSING, result.status());
    }

    @Test
    void shouldReturnNullWhenRecordDoesNotExist() {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("idem-key"))
                .thenReturn(null);

        IdempotencyRecord result = service.get("idem-key");

        assertNull(result);
    }

    @Test
    void shouldCompleteRecord() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingRef("BOOK-123");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(objectMapper.writeValueAsString(any(IdempotencyRecord.class)))
                .thenReturn("json");

        service.complete("idem-key", booking);

        verify(valueOperations).set(
                eq("idem-key"),
                eq("json"),
                any(Duration.class));
    }

    @Test
    void shouldRemoveRecord() {

        service.remove("idem-key");

        verify(redisTemplate).delete("idem-key");
    }

    @Test
    void shouldThrowWhenSerializationFails() throws Exception {

        when(objectMapper.writeValueAsString(any(IdempotencyRecord.class)))
                .thenThrow(JsonProcessingException.class);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.start("idem-key")
        );

        assertEquals(
                "Failed to serialize idempotency record",
                ex.getMessage()
        );
    }

    @Test
    void shouldThrowWhenDeserializationFails() throws Exception {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("idem-key"))
                .thenReturn("json");

        when(objectMapper.readValue("json", IdempotencyRecord.class))
                .thenThrow(JsonProcessingException.class);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.get("idem-key")
        );

        assertEquals(
                "Failed to deserialize idempotency record",
                ex.getMessage()
        );
    }
}