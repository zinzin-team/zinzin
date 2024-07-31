package com.fanclub.zinzin.domain.follow.controller;

import com.fanclub.zinzin.domain.follow.dto.AnswerFollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowingRequestResponse;
import com.fanclub.zinzin.domain.follow.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mates")
public class FollowController {

    private final FollowService followService;

    @GetMapping
    public ResponseEntity<?> getFollowList(HttpServletRequest request){
        Long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(followService.getFollowList(memberId));
    }

    @PostMapping
    public ResponseEntity<?> requestFollow(HttpServletRequest request, @RequestBody FollowRequest followRequest){
        Long memberId = (Long) request.getAttribute("memberId");
        followService.requestFollow(memberId, followRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> allowRejectFollow(HttpServletRequest request, @RequestBody AnswerFollowRequest answerFollowRequest){
        Long memberId = (Long) request.getAttribute("memberId");
        followService.answerFollowRequest(memberId, answerFollowRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> unfollow(HttpServletRequest request, @RequestBody FollowRequest followRequest){
        Long memberId = (Long) request.getAttribute("memberId");
        followService.unfollow(memberId, followRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FollowingRequestResponse>> getFollowRequestList(HttpServletRequest request){
        Long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(followService.getFollowRequestList(memberId));
    }
}
