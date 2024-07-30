package com.fanclub.zinzin.global.filter;

import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TokenAuthenticationFilter implements Filter {
    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        String authorizationHeader = httpRequest.getHeader("Authorization");
        String refreshTokenHeader = httpRequest.getHeader("RefreshToken");

        String accessToken = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(accessToken)) {
            if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer ")) {
                String refreshToken = refreshTokenHeader.substring(7);

                if (jwtUtil.validateRefreshToken(refreshToken)) {
                    Claims claims = jwtUtil.getClaims(refreshToken);
                    String email = claims.getSubject();
                    Role role = Role.valueOf((String) claims.get("role"));

                    String newAccessToken = jwtUtil.generateAccessToken(email, role);
                    String newRefreshToken = jwtUtil.generateRefreshToken(email, role);
                    httpResponse.setHeader("Authorization", "Bearer " + newAccessToken);
                    httpResponse.setHeader("RefreshToken", "Bearer " + newRefreshToken);

                    chain.doFilter(request, response);
                    return;
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("Unauthorized: Refresh token validation failed");
                    return;
                }
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Unauthorized: Access token validation failed");
                return;
            }
        }

    }
}

