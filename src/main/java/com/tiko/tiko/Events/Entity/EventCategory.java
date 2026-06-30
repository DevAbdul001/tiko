package com.tiko.tiko.Events.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "event_categories")
@NoArgsConstructor
public class EventCategory {

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    private List<Event> events = new ArrayList<>();

    public EventCategory(
            String name
    ){
        this.name = name;
    }
}
