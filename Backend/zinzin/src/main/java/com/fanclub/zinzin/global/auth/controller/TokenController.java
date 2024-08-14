package com.fanclub.zinzin.global.auth.controller;

import com.fanclub.zinzin.global.auth.dto.TokenDto;
import com.fanclub.zinzin.global.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> reGenerateToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(tokenService.refreshTokens(request, response));
    }
}
