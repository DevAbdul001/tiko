package com.tiko.tiko.Auth.Config;

import com.tiko.tiko.Auth.Services.JWTService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private String getTokenFromCookies(HttpServletRequest request){
        if(request.getCookies() == null){
            return  null;
        }
        for (Cookie cookie : request.getCookies()) {
            if("accessToken".equals(cookie.getName())){
                return  cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest  request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getTokenFromCookies(request);

        if(token != null && jwtService.isValid(token)) {
            Long userId = jwtService.extractUserId(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,null, Collections.emptyList()
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


}
