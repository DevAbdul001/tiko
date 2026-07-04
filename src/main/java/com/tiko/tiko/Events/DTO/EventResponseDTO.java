package com.tiko.tiko.Events.DTO;

import com.tiko.tiko.Events.Utils.EventStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public record EventResponseDTO(
        Long id,
        String organizerName,
        String name,
        Date date,
        String location,
        String imageUrl,
        EventStatus status
) {}
