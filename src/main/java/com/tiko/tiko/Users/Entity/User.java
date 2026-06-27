package com.tiko.tiko.Users.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    String password_hash;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;


    public User (String name , String email, String password_hash){
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
    }


}
