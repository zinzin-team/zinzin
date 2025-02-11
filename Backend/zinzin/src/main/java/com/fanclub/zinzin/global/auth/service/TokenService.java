package com.fanclub.zinzin.global.auth.service;

import com.fanclub.zinzin.global.auth.dto.TokenDto;
import com.fanclub.zinzin.global.error.code.TokenErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtUtil jwtUtil;

    public TokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public TokenDto refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookies(request);
        String authorizationHeader = request.getHeader("Authorization");

        String expiredAccessToken = extractToken(authorizationHeader);

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            String newAccessToken = jwtUtil.regenerateTokens(expiredAccessToken, refreshToken, response);

            return new TokenDto(newAccessToken);
        }
        throw new BaseException(TokenErrorCode.TOKEN_CANNOT_REFRESH);
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractToken(String header) {
        return header.substring(7);
    }
}
