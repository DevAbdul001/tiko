package com.tiko.tiko.Events.Entity;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.Users.Entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "description")
    String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory category;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EventTicketPrice> eventTicketPriceList = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Booking> bookings = new ArrayList<>();

    public Event(
            String name,
            User organizer,
            LocalDateTime date,
            EventCategory category,
            Long capacity,
            String location) {

        this.name = name;
        this.organizer = organizer;
        this.date = date;
        this.category = category;
        this.capacity = capacity;
        this.location = location;
        this.status = EventStatus.DRAFT;
    }

    public void addTicketPrice(EventTicketPrice ticketPrice) {
        eventTicketPriceList.add(ticketPrice);
        ticketPrice.setEvent(this);
    }
}
