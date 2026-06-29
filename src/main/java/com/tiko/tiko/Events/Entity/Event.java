package com.tiko.tiko.Events.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "events")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "event_name", nullable = false)
    String name;

    @Column(name = "date", nullable = false)
    Date date;

    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(name = "category", nullable = true)
    String description;

    @Column(name = "ticket_types", nullable = false)
    List<String> ticketTypes;

    @Column (name = "prices", nullable = false)
    List <BigDecimal> prices;

    @Column(name = "capacity", nullable = false)
    BigInteger capacity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_at", insertable = false, updatable = true)
    private LocalDateTime updatedAt;

    public Event(
            String name,
            Date date,
            Long categoryId,
            String description,
            List<String> ticketTypes,
            List <BigDecimal> prices,
            BigInteger capacity

    ){
        this.name = name;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
    }
}
