package com.roadsidehelp.api.config.security.jwt;

import com.roadsidehelp.api.feature.auth.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class JwtService {

    private final JwtProperties properties;
    private Key signingKey;
    private JwtParser jwtParser;

    // ======= CONSTRUCTOR =======
    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    // ======= INITIALIZE JWT SIGNING KEY & PARSER =======
    @PostConstruct
    public void init() {
        byte[] keyBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();
    }

    // ======= GENERATE ACCESS TOKEN =======
    public String generateAccessToken(String userId, String username, Collection<UserRole> roles) {
        Instant now = Instant.now();
        Instant exp = now.plus(properties.getAccessTokenValiditySeconds(), ChronoUnit.SECONDS);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userId)                     // userId as principal
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("type", "access")
                .claim("username", username)
                .claim("roles", roles)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ======= GENERATE REFRESH TOKEN =======
    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(properties.getRefreshTokenValidityDays(), ChronoUnit.DAYS);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("type", "refresh")
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ======= VALIDATE TOKEN SIGNATURE & STRUCTURE =======
    public boolean isValidateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // ======= PARSE ACCESS TOKEN (ACCESS TYPE ONLY) =======
    public JwtPayload parseAccessToken(String token) {

        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        // Prevent refresh token usage as access token
        String type = claims.get("type", String.class);
        if (!"access".equals(type)) {
            throw new JwtException("Invalid token type");
        }

        String userId = claims.getSubject();
        String username = claims.get("username", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) roles = List.of();

        return new JwtPayload(userId, username, roles);
    }

    // ======= TOKEN EXPIRATION CHECK =======
    public boolean isTokenExpired(String token) {
        Date exp = jwtParser.parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }

    // ======= GET TOKEN EXPIRATION DATE =======
    public Date getExpiration(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getExpiration();
    }

    // ======= TOKEN UTIL METHODS =======
    public String extractUserId(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    // ======= VALIDATE TOKEN AGAINST USER DETAILS =======
    public boolean isTokenValidForUser(String token, UserDetails userDetails) {
        String userId = extractUserId(token);

        return userId.equals(userDetails.getUsername())
                && isValidateToken(token)
                && !isTokenExpired(token);
    }
}
