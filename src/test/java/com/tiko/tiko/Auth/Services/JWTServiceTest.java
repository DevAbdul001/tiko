package com.tiko.tiko.Auth.Services;

import com.tiko.tiko.Users.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @Mock
    private JWTService jwtService;

    @Test
    void shouldGenerateAccessToken(){
        User user = new User(
                "Doe",
                "doe@app.com",
                "password"
        );

      user.setId(1L);
      String token  = jwtService.generateAccessToken(user);

      assertNotNull(token);
    }

    @Test
    void shouldGenerateRefreshToken(){
        User user = new User(
                "Doe",
                "doe@app.com",
                "password"
        );

        user.setId(1L);
        String token  = jwtService.generateRefreshToken(user);

        assertNotNull(token);
    }

    @Test
    void shouldExtractName(){
        User user = new User(
                "Doe",
                "doe@app.com",
                "password"
        );

        user.setId(1L);
        String token  = jwtService.generateAccessToken(user);

        Long userId = jwtService.extractUserId(token);

        assertNotNull(userId);
        assertEquals(1L, userId);
    }

    @Test
    void shouldReturnInvalid(){
        boolean valid = jwtService.isValid("xdgdzffdxcgvhbeszxrdcrtvybrxrctfvgzxdcfvg");

        assertFalse(valid);
    }
}
