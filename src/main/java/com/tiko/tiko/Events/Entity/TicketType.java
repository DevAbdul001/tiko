package com.tiko.tiko.Events.Entity;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table( name = "ticket_types")
@NoArgsConstructor
public class TicketType {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @OneToMany(mappedBy = "ticketType")
    private List<EventTicketPrice> eventTicketPrices = new ArrayList<>();


    public TicketType(
          String name
    ){
        this.name = name;
    }
}
