package com.fanclub.zinzin.global.filter;

import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.global.error.code.TokenErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TokenAuthenticationFilter implements Filter {
    private final JwtUtil jwtUtil;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/oauth2",
            "/api/login",
            "/api/mates/kakao",
            "/api/member/register"
    );

    @Autowired
    public TokenAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = ((HttpServletRequest) request).getRequestURI();
        if(EXCLUDE_URLS.stream().anyMatch(path::startsWith)){
            chain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = httpRequest.getHeader("Authorization");
            String refreshTokenHeader = httpRequest.getHeader("RefreshToken");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String accessToken = authorizationHeader.substring(7);

                if (jwtUtil.validateToken(accessToken)) {
                    Claims claims = jwtUtil.getClaims(accessToken);
                    request.setAttribute("memberId",claims.get("memberId", Long.class));
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

                    request.setAttribute("memberId",memberId);
                    chain.doFilter(request, response);
                    return;
                } else {
                    throw new BaseException(TokenErrorCode.TOKEN_NOT_FOUND);
                }
            }
            throw new BaseException(TokenErrorCode.TOKEN_NOT_FOUND);

        } catch (BaseException ex) {
            sendErrorResponse(httpResponse, ex);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, BaseException ex) throws IOException {
        response.setStatus(ex.getErrorCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(ex.getErrorCode().getStatus().value()));
        errorResponse.put("code", ex.getErrorCode().getCode());
        errorResponse.put("message", ex.getErrorCode().getMessage());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
