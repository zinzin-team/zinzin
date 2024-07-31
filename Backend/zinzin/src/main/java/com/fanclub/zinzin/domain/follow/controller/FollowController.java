package com.fanclub.zinzin.domain.follow.controller;

import com.fanclub.zinzin.domain.follow.dto.AnswerFollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowingListRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowingRequestResponse;
import com.fanclub.zinzin.domain.follow.service.FollowService;
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
    public ResponseEntity<?> getFollowList(@RequestBody FollowingListRequest request){
        return ResponseEntity.ok(followService.getFollowList(request));
    }

    @PostMapping
    public ResponseEntity<?> requestFollow(@RequestBody FollowRequest request){
        followService.requestFollow(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> allowRejectFollow(@RequestBody AnswerFollowRequest request){
        followService.answerFollowRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> unfollow(@RequestBody FollowRequest request){
        followService.unfollow(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FollowingRequestResponse>> getFollowRequestList(@RequestBody FollowingListRequest request){
        return ResponseEntity.ok(followService.getFollowRequestList(request));
    }
}
