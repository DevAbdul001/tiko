package com.tiko.tiko.Booking.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

   private  final StringRedisTemplate redisTemplate;

   public IdempotencyService(StringRedisTemplate redisTemplate){
       this.redisTemplate = redisTemplate;
   }

    public boolean acquire(String key) {

        return Boolean.TRUE.equals(

                redisTemplate
                        .opsForValue()
                        .setIfAbsent(
                                key,
                                "IN_PROGRESS",
                                Duration.ofHours(24)
                        )
        );
    }

}
