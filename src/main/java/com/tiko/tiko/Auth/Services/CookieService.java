package com.tiko.tiko.Auth.Services;


import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class CookieService {

    private JWTService jwtService;

    public ResponseCookie generateAccessCookie(String token){
        return ResponseCookie.from("accessToken",  token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("true")
                .build();
    }

    public ResponseCookie generateRefreshCookie(String token){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60 * 1000)
                .sameSite("true")
                .build();
    }
}
