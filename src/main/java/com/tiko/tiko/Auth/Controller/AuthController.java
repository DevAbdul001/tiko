package com.tiko.tiko.Auth.Controller;

import com.tiko.tiko.Auth.DTO.AuthResponseDTO;
import com.tiko.tiko.Auth.DTO.LoginDTO;
import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Auth.Services.AuthService;
import com.tiko.tiko.Auth.Services.CookieService;
import com.tiko.tiko.Auth.Services.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final CookieService cookieService;

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
            @RequestBody LoginDTO dto ,
            HttpServletResponse response
            ) {
        AuthResponseDTO result = authService.login(dto);
        String accessToken = jwtService.generateAccessToken(result);
        String refreshToken = jwtService.generateRefreshToken(result);

        ResponseCookie accessCookie = cookieService.generateAccessCookie(accessToken);
        ResponseCookie refreshCookie = cookieService.generateRefreshToken(refreshToken);

        response.addHeader("Set-cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return result;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        Long userId = jwtService.extractUserId(refreshToken);
        AuthResponseDTO dto = authService.fetchById(userId);

        String accessToken = jwtService.generateAccessToken(dto);
        ResponseCookie accessCookie = cookieService.generateAccessCookie(accessToken);

        response.addHeader("Set-Cookie", accessCookie.toString());

        return  ResponseEntity.ok().build();
    }



}
