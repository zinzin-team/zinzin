package com.fanclub.zinzin.domain.reward.controller;

import com.fanclub.zinzin.domain.reward.dto.SuccessCountResponse;
import com.fanclub.zinzin.domain.reward.service.RewardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/success-count")
public class RewardController {

    private final RewardService rewardService;

    @GetMapping
    public ResponseEntity<SuccessCountResponse> getSuccessCount(HttpServletRequest request) {
        SuccessCountResponse successCountResponse = rewardService.getSuccessCount((Long) request.getAttribute("memberId"));
        return ResponseEntity.ok(successCountResponse);
    }
}
