package com.example.JwtAuthentication.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    private JwtUtils(){}

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static  final String ISSUER = "JwtAuthentication";


    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        try {
            return Optional.of(Jwts.parser().verifyWith(secretKey)
                    .build().parseSignedClaims(jwtToken).getPayload());
        } catch (IllegalArgumentException | JwtException e) {
            log.error("JWT Exception occurred");
        }
        return Optional.empty();
    }

    public static Optional<String> extractUsername(String jwtToken) {
        var claimsOptional=parseToken(jwtToken);
       claimsOptional.ifPresent(claims->log.info(claims.getSubject()));
        return claimsOptional.map(Claims::getSubject);
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .compact();
    }
}
