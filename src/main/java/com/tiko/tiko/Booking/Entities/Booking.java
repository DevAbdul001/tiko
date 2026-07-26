package com.tiko.tiko.Booking.Entities;

import com.tiko.tiko.Booking.Enums.BookingStatus;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Users.Entity.User;
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
@NoArgsConstructor
@Table( name = "bookings")
public class Booking {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name = "booking_reference", nullable = false, unique = true)
    private String bookingRef;

    @Column( name = "total_amount", nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column ( name ="status", nullable = false)
    private BookingStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn( name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false)
    private User user;

    @OneToMany (
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookingItem> bookingItems = new ArrayList<>();

    public void addBookingItem( BookingItem item){
        bookingItems.add(item);
        item.setBooking(this);
    }

    public void removeBookingItem( BookingItem item ){
        bookingItems.remove( item );
        item.setBooking(null);
    }

    public Booking(
            String bookingRef,
            Long totalAmount,
            Event event,
            User user
    ){
        this.bookingRef = bookingRef;
        this.totalAmount = totalAmount;
        this.event = event;
        this.user = user;
    }
}
