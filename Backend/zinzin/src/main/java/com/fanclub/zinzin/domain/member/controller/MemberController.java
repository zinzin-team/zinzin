package com.fanclub.zinzin.domain.member.controller;

import com.fanclub.zinzin.domain.member.dto.CheckSearchIdResponse;
import com.fanclub.zinzin.domain.member.dto.MatchingModeRequest;
import com.fanclub.zinzin.domain.member.dto.MemberInfoResponse;
import com.fanclub.zinzin.domain.member.dto.MemberInfoUpdateRequest;
import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
                                           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
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
