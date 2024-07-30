package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/authorize")
    public ResponseEntity<String> getKakaoAuthorizeUrl() {
        return ResponseEntity.ok(oAuth2Service.getKakaoAuthorizeUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {
        ArrayList<String> tokens = oAuth2Service.getKakaoTokens(code);

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);
        String idToken = tokens.get(2);

        String[] claims = jwtUtil.getSubAndEmailFromIdToken(idToken);
        String sub = claims[0];
        String email = claims[1];

        Member member = memberRepository.findBySub(sub);

        if (member != null) {
            String ourAccessToken = jwtUtil.generateAccessToken(email, Role.USER);
            String ourRefreshToken = jwtUtil.generateRefreshToken(email, Role.USER);
            Map<String, String> tokensMap = new HashMap<>();
            tokensMap.put("accessToken", ourAccessToken);
            tokensMap.put("refreshToken", ourRefreshToken);
            return ResponseEntity.ok(tokensMap);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "유저를 찾을 수 없습니다. 회원가입 페이지로 안내합니다");
            response.put("sub", sub); // 클라이언트에서 회원가입 시 사용할 수 있는 정보 제공
            response.put("email", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
