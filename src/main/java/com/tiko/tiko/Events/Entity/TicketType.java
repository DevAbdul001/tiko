package com.tiko.tiko.Events.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

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

    public TicketType(
          String name
    ){
        this.name = name;
    }
}
