package com.fanclub.zinzin.domain.follow.controller;

import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mates")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<?> requestFollow(@RequestBody FollowRequest request){
        followService.requestFollow(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
