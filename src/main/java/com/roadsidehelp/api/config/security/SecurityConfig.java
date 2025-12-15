package com.roadsidehelp.api.config.security;

import com.roadsidehelp.api.config.security.jwt.JwtAuthenticationEntryPoint;
import com.roadsidehelp.api.config.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ============== PUBLIC ENDPOINTS ==============
        String[] publicUris = {
                "/api/v1/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/favicon.ico",
                "/ws-connect/**"
        };

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ---------- Public APIs ----------
                        .requestMatchers(publicUris).permitAll()

                        // ---------- Garage Module ----------
                        // Create Garage (USER or ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/garage")
                        .hasAnyRole("USER", "ADMIN")

                        // Update Garage (GARAGE or ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/garage/**")
                        .hasAnyRole("GARAGE", "ADMIN")

                        // Get garage details by ID, city, nearest → PUBLIC
                        .requestMatchers(HttpMethod.GET, "/api/v1/garage/**")
                        .permitAll()

                        // ---------- All other requests ----------
                        .anyRequest().authenticated()
                );

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
