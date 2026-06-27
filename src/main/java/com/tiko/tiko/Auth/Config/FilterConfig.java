package com.tiko.tiko.Auth.Config;

import com.tiko.tiko.Auth.Config.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistration() {

        FilterRegistrationBean<JWTFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(jwtFilter);

        registration.addUrlPatterns(
                "/api/v1/auth/refresh",
                "/api/v1/auth/logout"
        );

        return registration;
    }
}