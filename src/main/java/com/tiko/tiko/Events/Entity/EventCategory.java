package com.tiko.tiko.Events.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "event_categories")
@NoArgsConstructor
public class EventCategory {

    @Column
    BigInteger id;

    @Column
    String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public EventCategory(
            BigInteger id,
            String name
    ){
        this.id = id;
        this.name = name;
    }
}
