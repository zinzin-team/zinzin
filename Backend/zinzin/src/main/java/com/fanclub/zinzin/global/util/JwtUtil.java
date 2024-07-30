package com.fanclub.zinzin.global.util;

import com.fanclub.zinzin.global.error.code.TokenErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Value("${spring.jwt.token.secret-key}")
    private String secretKeyString;
    @Value("${spring.jwt.access.expiration}")
    private long accessExpiration;
    @Value("${spring.jwt.refresh.expiration}")
    private long refreshExpiration;

    public String generateAccessToken(String id, String role) {
        if (id == null || role == null) {
            throw new BaseException(TokenErrorCode.INVALID_INPUT);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", id);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpiration);

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(id)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKeyString)
                    .compact();
        } catch (Exception e) {
            throw new BaseException(TokenErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    public String generateRefreshToken(String id, String role) {
        if (id == null || role == null) {
            throw new BaseException(TokenErrorCode.INVALID_INPUT);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", id);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpiration);

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(id)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKeyString)
                    .compact();
        } catch (Exception e) {
            throw new BaseException(TokenErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKeyString).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, String email) {
        return (extractUsername(token).equals(email) && !isTokenExpired(token));
    }

    // idToken 유효성 검증
    public boolean validateToken(String idToken) {
        return true;
    }

    public static Long getSubFromIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid ID token format.");
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            return (Long) payloadMap.get("sub");
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode ID token", e);
        }
    }

    public String getEmailFromIdToken(String idToken) {
    }
}
