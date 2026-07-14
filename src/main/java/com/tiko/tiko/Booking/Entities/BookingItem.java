package com.tiko.tiko.Booking.Entities;

import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table( name = "booking_items")
public class BookingItem {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    @Positive
    @Min(1)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    @PositiveOrZero
    private int unitPrice;

    @Column(name = "ticket_name", nullable = false)
    private String ticketName;

    @ManyToOne
    @JoinColumn( name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn( name = "event_ticket_type_id", nullable = false)
    private EventTicketPrice eventTicketPrice;

    public BookingItem(
            int quantity,
            int unitPrice,
            String ticketName,
            Booking booking,
            EventTicketPrice eventTicketPrice
    ){
      this.quantity = quantity;
      this.unitPrice = unitPrice;
      this.ticketName = ticketName;
      this.booking = booking;
      this.eventTicketPrice = eventTicketPrice;
    }
}
