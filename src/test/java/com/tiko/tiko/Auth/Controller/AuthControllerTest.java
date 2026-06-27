package com.tiko.tiko.Auth.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiko.tiko.Auth.Config.JWTFilter;
import com.tiko.tiko.Auth.DTO.AuthResponseDTO;
import com.tiko.tiko.Auth.DTO.LoginDTO;
import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Auth.Services.AuthService;
import com.tiko.tiko.Auth.Services.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    JWTService jwtService;

    @MockitoBean
    JWTFilter jwtFilter;

    @Test
    void shouldRegister() throws Exception {

        RegisterDTO dto = new RegisterDTO(
                "Doe",
                "doe@email.com",
                "password"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(authService).registerUser(any(RegisterDTO.class));
    }

    @Test
    void shouldLogin() throws Exception {

        LoginDTO dto = new LoginDTO(
                "doe@email.com",
                "password"
        );

        AuthResponseDTO auth = new AuthResponseDTO(
                1L,
                "Doe",
                "doe@email.com"
        );

        when(authService.login(any(LoginDTO.class)))
                .thenReturn(auth);

        when(jwtService.generateAccessToken(any()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(any()))
                .thenReturn("refresh-token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Doe"))
                .andExpect(jsonPath("$.email").value("doe@email.com"));

        verify(authService).login(any(LoginDTO.class));
        verify(jwtService).generateAccessToken(any());
        verify(jwtService).generateRefreshToken(any());
    }

    @Test
    void shouldRefreshToken() throws Exception {

        AuthResponseDTO auth = new AuthResponseDTO(
                1L,
                "Doe",
                "doe@email.com"
        );

        when(jwtService.extractUserId("refresh-token"))
                .thenReturn(1L);

        when(authService.fetchById(1L))
                .thenReturn(auth);

        when(jwtService.generateAccessToken(any()))
                .thenReturn("new-access-token");

        mockMvc.perform(post("/api/v1/auth/refreshToken")
                        .cookie(new jakarta.servlet.http.Cookie(
                                "refreshToken",
                                "refresh-token")))
                .andExpect(status().isOk());

        verify(jwtService).extractUserId("refresh-token");
        verify(authService).fetchById(1L);
        verify(jwtService).generateAccessToken(any());
    }
}