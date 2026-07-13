package com.tiko.tiko.Users.Entity;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Events.Entity.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column( name =  "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "organizer")
    private List<Event> events = new ArrayList<>();

    @OneToMany( mappedBy = "user")
    private List<Booking> bookings = new ArrayList<>();

    public User (String name , String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }


}
