package com.tiko.tiko.Events.DTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public record EventResponseDTO(
        Long id,
        String name,
        Date date,
        String description,
        List<BigDecimal> prices,
        BigInteger capacity
) {}
