package com.tiko.tiko.Auth.Controller;

import com.tiko.tiko.Auth.DTO.AuthResponseDTO;
import com.tiko.tiko.Auth.DTO.LoginDTO;
import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Auth.Services.AuthService;
import com.tiko.tiko.Auth.Services.JWTService;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody RegisterDTO dto
            ){
        authService.registerUser(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDTO login(
            @RequestBody LoginDTO dto,
            HttpServletResponse response
            ){
        AuthResponseDTO auth = authService.login(dto);

        String accessToken = jwtService.generateAccessToken(auth);
        String refreshToken = jwtService.generateRefreshToken(auth);

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken);

        return auth;
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<Void> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        Long userId = jwtService.extractUserId(refreshToken);
        AuthResponseDTO auth = authService.fetchById(userId);
        String cookie = jwtService.generateAccessToken(auth);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refreshToken")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }

}
