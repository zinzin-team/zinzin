package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.auth.dto.MemberAuthResponseDto;
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
    public ResponseEntity<MemberAuthResponseDto> kakaoCallback(@RequestParam("code") String code) {
        ArrayList<String> tokens = oAuth2Service.getKakaoTokens(code);
        String idToken = tokens.get(2);

        String[] claims = jwtUtil.getSubAndEmailFromIdToken(idToken);
        String sub = claims[0];
        String email = claims[1];

        Member member = memberRepository.findBySub(sub);

        if (member != null) {
            Long memberId = member.getId();
            Map<String, String> tokensMap = oAuth2Service.generateTokens(memberId, sub, Role.USER);
            String accessToken = tokensMap.get("accessToken");
            String refreshToken = tokensMap.get("refreshToken");

            return ResponseEntity.ok(MemberAuthResponseDto.createTokenResponse(accessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MemberAuthResponseDto.createRegisterResponse(sub, email));
    }
}
