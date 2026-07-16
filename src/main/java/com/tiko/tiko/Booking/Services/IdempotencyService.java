package com.tiko.tiko.Booking.Services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiko.tiko.Booking.DTOs.IdempotencyRecord;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

   private  final StringRedisTemplate redisTemplate;
   private final ObjectMapper objectMapper;

   public IdempotencyService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper){

       this.redisTemplate = redisTemplate;
       this.objectMapper = objectMapper;
   }

    public boolean start(String key) {

        IdempotencyRecord record = new IdempotencyRecord(
                key,
                IdempotencyStatus.PROCESSING,
                null,
                null
        );

        try {

            String json = objectMapper.writeValueAsString(record);

            return Boolean.TRUE.equals(
                    redisTemplate.opsForValue().setIfAbsent(
                            key,
                            json,
                            Duration.ofHours(24)
                    )
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize idempotency record", e);
        }
    }

    public IdempotencyRecord get(String key) {

        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, IdempotencyRecord.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize idempotency record", e);
        }
    }

    public void complete(String key, Booking booking) {

        IdempotencyRecord record = new IdempotencyRecord(
                key,
                IdempotencyStatus.COMPLETED,
                booking.getId(),
                booking.getBookingRef()
        );

        try {

            String json = objectMapper.writeValueAsString(record);

            redisTemplate.opsForValue().set(
                    key,
                    json,
                    Duration.ofHours(24)
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize idempotency record", e);
        }
    }

}
