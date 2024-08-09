package com.fanclub.zinzin.domain.member.controller;

import com.fanclub.zinzin.domain.member.dto.*;
import com.fanclub.zinzin.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/register/search-id/{searchId}")
    public ResponseEntity<CheckSearchIdResponse> checkDuplicatedSearchIdForRegist(@PathVariable(name = "searchId") String searchId){
        return ResponseEntity.ok(memberService.checkDuplicatedSearchId(searchId));
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

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getOwnInfo(HttpServletRequest request){
        return ResponseEntity.ok(memberService.getMemberInfo(request));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateOwnInfo(HttpServletRequest request,
                                           @RequestPart("profileImage") MultipartFile profileImage,
                                           @RequestPart("searchId") String searchId) {
        memberService.updateMemberInfo((Long) request.getAttribute("memberId"), new MemberInfoUpdateRequest(profileImage, searchId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteOwnInfo(HttpServletRequest request) {
        memberService.withdraw((Long) request.getAttribute("memberId"));
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    @PostMapping("/nickname")
    public ResponseEntity<String> updateRandomNickname(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.updateRandomNickname((Long) request.getAttribute("memberId")));
    }
}
