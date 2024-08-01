package com.fanclub.zinzin.domain.member.controller;

import com.fanclub.zinzin.domain.member.dto.CheckSearchIdResponse;
import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

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
}