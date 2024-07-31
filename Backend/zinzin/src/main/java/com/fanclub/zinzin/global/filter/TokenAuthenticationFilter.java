package com.fanclub.zinzin.global.filter;

import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter implements Filter {
    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authorizationHeader = httpRequest.getHeader("Authorization");
        String refreshTokenHeader = httpRequest.getHeader("RefreshToken");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);

            if (jwtUtil.validateToken(accessToken)) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer ")) {
            String refreshToken = refreshTokenHeader.substring(7);

            if (jwtUtil.validateRefreshToken(refreshToken)) {
                Claims claims = jwtUtil.getClaims(refreshToken);
                Long memberId = claims.get("memberId", Long.class);
                String email = claims.getSubject();
                Role role = Role.valueOf((String) claims.get("role"));

                String newAccessToken = jwtUtil.generateAccessToken(memberId, email, role);
                String newRefreshToken = jwtUtil.generateRefreshToken(memberId, email, role);
                httpResponse.setHeader("Authorization", "Bearer " + newAccessToken);
                httpResponse.setHeader("RefreshToken", "Bearer " + newRefreshToken);

                chain.doFilter(request, response);
                return;
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Unauthorized: Refresh token validation failed");
                return;
            }
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Unauthorized: Access token validation failed");
    }

}

