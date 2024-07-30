package com.fanclub.zinzin.global.util;

import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.global.error.code.TokenErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
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

    @Value("${jwt.token.secret-key}")
    private String secretKeyString;
    @Value("${jwt.access.expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public String generateAccessToken(String sub, Role role) {
        if (sub == null || role == null) {
            throw new BaseException(TokenErrorCode.INVALID_INPUT);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpiration);

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(sub)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKeyString)
                    .compact();
        } catch (Exception e) {
            throw new BaseException(TokenErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    public String generateRefreshToken(String sub, Role role) {
        if (sub == null || role == null) {
            throw new BaseException(TokenErrorCode.INVALID_INPUT);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpiration);

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(sub)
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

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKeyString)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰에서도 클레임을 얻을 수 있음
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // idToken 유효성 검증은 추후 구현 예정
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKeyString)
                    .parseClaimsJws(token)
                    .getBody();
            if (isTokenExpired(token)) {
                return false;
            }
            return true;
        } catch (SignatureException | ExpiredJwtException e) {
            return false;
        }
    }

    public static String[] getSubAndEmailFromIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                throw new BaseException(TokenErrorCode.ID_TOKEN_FORMAT_ERROR);
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            String sub = (String) payloadMap.get("sub");
            String email = (String) payloadMap.get("email");
            return new String[]{sub, email};
        } catch (Exception e) {
            throw new BaseException(TokenErrorCode.TOKEN_DECODE_FAILED);
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken);
    }
}
