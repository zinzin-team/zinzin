package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.error.code.AuthErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
//  public final KakaoFriendsService kakaoFriendsService;

    @GetMapping("/authorize")
    public ResponseEntity<String> getKakaoAuthorizeUrl() {
        return ResponseEntity.ok(oAuth2Service.getKakaoAuthorizeUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, Model model) {
        ArrayList<String> tokens = oAuth2Service.getKakaoTokens(code);

        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);
        String idToken = tokens.get(2);

        String kakaoId = jwtUtil.getSubFromIdToken(idToken);
        System.out.println(kakaoId);

        return ResponseEntity.ok(kakaoId);
    }
}
