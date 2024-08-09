package com.fanclub.zinzin.domain.matchingstatus.controller;

import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingStatusResponse;
import com.fanclub.zinzin.domain.matchingstatus.service.MatchingStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matching-list")
@RequiredArgsConstructor
public class MatchingStatusController {
    private final MatchingStatusService matchingStatusService;

    @GetMapping
    public ResponseEntity<List<MatchingStatusResponse>> getMatchingStatusList(HttpServletRequest request){
        return ResponseEntity.ok(matchingStatusService.getMatchingStatusList(request));
    }
}
