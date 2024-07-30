package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.error.code.AuthErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-url}")
    private String authUrl;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    @Value("${kakao.scope}")
    private String scope;

    public String getKakaoAuthorizeUrl() {
        return UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .toUriString();
    }

    public ArrayList<String> getKakaoTokens(String code) {
        try {
            String tokenRequestUrl = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("code", code)
                    .queryParam("client_secret", clientSecret)
                    .toUriString();

            Map<String, String> response = restTemplate.postForObject(tokenRequestUrl, null, Map.class);
            ArrayList<String> tokens = new ArrayList<>();
            tokens.add(response.get("access_token"));
            tokens.add(response.get("refresh_token"));
            tokens.add(response.get("id_token"));
            return tokens;
        } catch (Exception e) {
            throw new BaseException(AuthErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
    }

    public Map<String, String> generateTokens(String sub, Role role) {
        String accessToken = jwtUtil.generateAccessToken(sub, role.USER);
        String refreshToken = jwtUtil.generateRefreshToken(sub, role.USER);
        Map<String, String> tokensMap = new HashMap<>();
        tokensMap.put("accessToken", accessToken);
        tokensMap.put("refreshToken", refreshToken);
        tokensMap.put("isUser", Boolean.toString(true));
        return tokensMap;
    }

    public Map<String, String> makeRegisterResponse(String sub, String email) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "유저를 찾을 수 없습니다. 회원가입 페이지로 안내합니다");
        response.put("sub", sub);
        response.put("email", email);
        response.put("isUser", Boolean.toString(false));
        return response;
    }
}
