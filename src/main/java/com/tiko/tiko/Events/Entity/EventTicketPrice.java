package com.tiko.tiko.Events.Entity;

import com.tiko.tiko.Booking.Entities.BookingItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name ="event_ticket_prices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventTicketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "remaining_tickets", nullable = false)
    private int remainingTickets;

   @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

   @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

   @OneToMany( mappedBy = "eventTicketPrice")
    List<BookingItem> bookingItems = new ArrayList<>();

   public EventTicketPrice(
           Long price,
           int quantity,
           TicketType ticketType
   ){
       this.price = price;
       this.quantity = quantity;
       this.ticketType = ticketType;
   }

    public void initializeRemainingTickets() {
        this.remainingTickets = this.quantity;
    }

}
