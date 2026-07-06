package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Utils.EventStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record EventResponseDTO(
        Long id,
        String organizerName,
        String name,
        LocalDateTime date,
        String location,
        String imageUrl,
        EventStatus status
) {}
