package com.tiko.tiko.Events.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name ="event_ticket_prices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventTicketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Price in integer instead of BigInteger since price is stored in cents
    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

   @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

   @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

   public EventTicketPrice(
           int price,
           int quantity,
           TicketType ticketType
   ){
       this.price = price;
       this.quantity = quantity;
       this.ticketType = ticketType;
   }

}
