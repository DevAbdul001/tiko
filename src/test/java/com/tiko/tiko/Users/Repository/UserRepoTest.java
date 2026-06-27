package com.tiko.tiko.Users.Repository;

import com.tiko.tiko.Users.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void shouldFetchUserByEmail(){
        User user = new User(
                "Doe", "doe@app.com", "hash"
        );

        userRepo.save(user);
        Optional<User> response = userRepo.findByEmail("doe@app.com");

        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals("Doe", response.get().getName());
        assertEquals("doe@app.com", response.get().getEmail());
    }

    @Test
    void shouldCheckIfEmailExists(){
        User user = new User(
                "Doe", "doe@app.com", "hash"
        );

        userRepo.save(user);

        boolean exists = userRepo.existsByEmail("doe@app.com");
        boolean notExists = userRepo.existsByEmail("test@app.com");

        assertTrue(exists);
        assertFalse(notExists);
    }
}
