package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.auth.dto.MemberAuthResponseDto;
import com.fanclub.zinzin.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/authorize")
    public void getKakaoAuthorizeUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuth2Service.getKakaoAuthorizeUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<MemberAuthResponseDto> kakaoCallback(@RequestParam("code") String code) {
        ArrayList<String> tokens = oAuth2Service.getKakaoTokens(code);
        String kakaoAccessToken = tokens.get(0);
        String idToken = tokens.get(2);

        String[] claims = jwtUtil.getSubAndEmailFromIdToken(idToken);
        String sub = claims[0];

        Member member = memberRepository.findBySub(sub);
        return ResponseEntity.ok(oAuth2Service.loginOrRegister(member, claims, kakaoAccessToken));
    }
}
