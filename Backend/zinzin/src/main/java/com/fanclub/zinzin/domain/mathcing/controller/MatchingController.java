package com.fanclub.zinzin.domain.mathcing.controller;

import com.fanclub.zinzin.domain.mathcing.dto.MatchingResponse;
import com.fanclub.zinzin.domain.mathcing.service.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matchings")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @GetMapping
    public ResponseEntity<MatchingResponse> getMatchings(HttpServletRequest request){
        return ResponseEntity.ok(matchingService.getMatchings(request));
    }
}
