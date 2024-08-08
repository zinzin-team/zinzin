package com.fanclub.zinzin.domain.matching.controller;

import com.fanclub.zinzin.domain.matching.dto.CheckingRequest;
import com.fanclub.zinzin.domain.matching.dto.MatchingResponse;
import com.fanclub.zinzin.domain.matching.service.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchings")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @GetMapping
    public ResponseEntity<MatchingResponse> getMatchings(HttpServletRequest request){
        return ResponseEntity.ok(matchingService.getMatchings(request));
    }

    @PostMapping("like")
    public ResponseEntity<?> checkCard(HttpServletRequest request, @RequestBody CheckingRequest checkingRequest){
        return ResponseEntity.ok(matchingService.checkCard(request, checkingRequest));
    }
}
