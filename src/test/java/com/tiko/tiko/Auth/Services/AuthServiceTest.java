package com.tiko.tiko.Auth.Services;

import com.tiko.tiko.Auth.DTO.AuthResponseDTO;
import com.tiko.tiko.Auth.DTO.LoginDTO;
import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;


    @Test
    void shouldRegisterUser(){
        RegisterDTO dto = new RegisterDTO(
                "Doe", "doe@app.com", "password"
        );
        User user = new User(
                dto.name(),
                dto.email(),
                "passwordHash"
        );

        when(passwordEncoder.encode(dto.password()))
                .thenReturn("hashed_password");
        when(userRepo.save(any(User.class)))
                .thenReturn(user);
        when(userRepo.existsByEmail(dto.email()))
                .thenReturn(false);

        User result = authService.registerUser(dto);
        System.out.println(result);

        assertNotNull(result);
        assertEquals("Doe", result.getName());
        assertEquals("doe@app.com", result.getEmail());
        verify(userRepo).existsByEmail(any(String.class));
    }

    @Test
    void shouldLoginUser(){
        LoginDTO dto = new LoginDTO(
                "doe@app.com", "password"
        );
        User user = new User(
                "Doe",
                dto.email(),
                "password"
        );


        when(userRepo.findByEmail(dto.email()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(),"password"))
                .thenReturn(true);

        AuthResponseDTO responseDTO =authService.login(dto);

        assertNotNull(responseDTO);
        assertEquals("Doe", responseDTO.name());
        assertEquals("doe@app.com", responseDTO.email());
        verify(userRepo).findByEmail(any(String.class));
    }
}
