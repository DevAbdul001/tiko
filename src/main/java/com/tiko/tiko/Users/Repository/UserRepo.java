package com.tiko.tiko.Users.Repository;

import com.tiko.tiko.Users.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo  extends JpaRepository <User, Long>{

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
