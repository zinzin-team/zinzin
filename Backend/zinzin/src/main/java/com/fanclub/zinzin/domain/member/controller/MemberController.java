package com.fanclub.zinzin.domain.member.controller;

import com.fanclub.zinzin.domain.member.dto.MatchingModeRequest;
import com.fanclub.zinzin.domain.member.dto.CheckSearchIdResponse;
import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.registerNewMember(memberRegisterDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/search-id/{searchId}")
    public ResponseEntity<CheckSearchIdResponse> checkDuplicatedSearchId(@PathVariable(name = "searchId") String searchId){
        return ResponseEntity.ok(memberService.checkDuplicatedSearchId(searchId));
    }

    @PostMapping("/matching-mode")
    public ResponseEntity<?> changeMatchingMode(HttpServletRequest request, @RequestBody MatchingModeRequest matchingModeRequest) {
        memberService.changeMatchingMode((Long) request.getAttribute("memberId"), matchingModeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}