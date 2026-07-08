package com.tiko.tiko.Auth.Controller;

import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Auth.Services.AuthService;
import com.tiko.tiko.Auth.Services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



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



}
