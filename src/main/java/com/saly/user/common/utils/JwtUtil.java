package com.saly.user.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class JwtUtil {

    public static Optional<String> generateToken(final String secret, final long tokenValidity, final String subject) {
        final Map<String, Object> claims = new HashMap<>();

        try {
            return Optional.ofNullable(doGenerateToken(secret, tokenValidity, claims, subject));
        } catch (Exception e) {
            log.warn("Can't generate jwt token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static String doGenerateToken(final String secret, final long tokenValidity,
                                          Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public static Optional<String> fetchSubject(String token, String secret) {
        try {
            return Optional.ofNullable(getClaimFromToken(token, secret, Claims::getSubject));
        } catch (Exception e) {
            log.warn("Can't fetch subject from jwt token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static <T> T getClaimFromToken(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
